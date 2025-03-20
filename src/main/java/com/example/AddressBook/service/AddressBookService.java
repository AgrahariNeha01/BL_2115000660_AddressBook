//package com.example.AddressBook.service;
//
//import com.example.AddressBook.dto.AddressBookDTO;
//import com.example.AddressBook.model.AddressBookModel;
//import com.example.AddressBook.repository.AddressBookRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@Service
//public class AddressBookService implements IAddressBookService {
//
//    private final AddressBookRepository repo;
//    private static final String CACHE_KEY = "addresses";
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Autowired
//    public AddressBookService(AddressBookRepository repo) {
//        this.repo = repo;
//    }
//
//    @Override
//    public void add(AddressBookDTO dto) {
//        AddressBookModel model = new AddressBookModel(dto);
//        repo.save(model);
//    }
//
//    @Override
//    public List<AddressBookModel> getAll() {
//        return repo.findAll();
//    }
//
//    @Override
//    public Optional<AddressBookModel> getById(int id) {
//        return repo.findById(id);
//    }
//
//    @Override
//    public boolean update(int id, AddressBookDTO dto) {
//        Optional<AddressBookModel> existing = repo.findById(id);
//        if (existing.isPresent()) {
//            AddressBookModel model = existing.get();
//            model.setName(dto.getName());
//            model.setPhone(dto.getPhone());
//            repo.save(model);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean delete(int id) {
//        if (repo.existsById(id)) {
//            repo.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void addEntry(String name, String phone) {
//        AddressBookModel model = new AddressBookModel(name, phone);
//        repo.save(model);
//        log.info("Entry added: {} - {}", name, phone);
//    }
//
//    @Override
//    public void deleteEntry(String name) {
//        Optional<AddressBookModel> entry = repo.findByName(name);
//        entry.ifPresent(repo::delete);
//        log.info("Entry deleted: {}", name);
//    }
//
//    // Redis methods
//    @Override
//    public void saveToCache(int id, AddressBookModel model) {
//        redisTemplate.opsForHash().put(CACHE_KEY, String.valueOf(id), model);
//        log.info("Saved to cache: {}", model);
//    }
//
//    @Override
//    public Optional<AddressBookModel> getFromCache(int id) {
//        Object obj = redisTemplate.opsForHash().get(CACHE_KEY, String.valueOf(id));
//        if (obj instanceof AddressBookModel) {
//            return Optional.of((AddressBookModel) obj);
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    public void deleteFromCache(int id) {
//        redisTemplate.opsForHash().delete(CACHE_KEY, String.valueOf(id));
//        log.info("Deleted from cache: {}", id);
//    }
//}
package com.example.AddressBook.service;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBookModel;
import com.example.AddressBook.repository.AddressBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AddressBookService implements IAddressBookService {

    private static final String CACHE_KEY = "addresses";
    private final AddressBookRepository repo;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public AddressBookService(AddressBookRepository repo, RedisTemplate<String, Object> redisTemplate) {
        this.repo = repo;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void add(AddressBookDTO dto) {
        AddressBookModel model = new AddressBookModel(dto);
        repo.save(model);
        redisTemplate.delete(CACHE_KEY); // Clear cache after adding a new entry
    }

    @Override
    public List<AddressBookModel> getAll() {
        List<AddressBookModel> addresses = (List<AddressBookModel>) redisTemplate.opsForValue().get(CACHE_KEY);
        if (addresses == null) {
            addresses = repo.findAll();
            redisTemplate.opsForValue().set(CACHE_KEY, addresses, 10, TimeUnit.MINUTES);
        }
        return addresses;
    }

    @Override
    public Optional<AddressBookModel> getById(int id) {
        String key = CACHE_KEY + id;
        Optional<AddressBookModel> cachedEntry = (Optional<AddressBookModel>) redisTemplate.opsForValue().get(key);
        if (cachedEntry == null) {
            cachedEntry = repo.findById(id);
            cachedEntry.ifPresent(entry -> redisTemplate.opsForValue().set(key, entry, 10, TimeUnit.MINUTES));
        }
        return cachedEntry;
    }

    @Override
    public boolean update(int id, AddressBookDTO dto) {
        Optional<AddressBookModel> existing = repo.findById(id);
        if (existing.isPresent()) {
            AddressBookModel model = existing.get();
            model.setName(dto.getName());
            model.setPhone(dto.getPhone());
            repo.save(model);
            redisTemplate.delete(CACHE_KEY); // Clear cache after updating
            redisTemplate.delete(CACHE_KEY + id);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            redisTemplate.delete(CACHE_KEY); // Clear cache after deletion
            redisTemplate.delete(CACHE_KEY + id);
            return true;
        }
        return false;
    }

    @Override
    public void addEntry(String name, String phone) {
        AddressBookModel model = new AddressBookModel(name, phone);
        repo.save(model);
        redisTemplate.delete(CACHE_KEY);
        log.info("Entry added: {} - {}", name, phone);
    }

    @Override
    public void deleteEntry(String name) {
        Optional<AddressBookModel> entry = repo.findByName(name);
        entry.ifPresent(repo::delete);
        redisTemplate.delete(CACHE_KEY);
        log.info("Entry deleted: {}", name);
    }
}

