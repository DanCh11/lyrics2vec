package de.daycu.springLyrics2Vec.services;

import de.daycu.springLyrics2Vec.models.Record;
import de.daycu.springLyrics2Vec.repositories.RecordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RecordService {

    @Autowired
    private RecordRepository repository;

    public Record findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Record by id: " + id + "was not found."));
    }

    public List<Record> findAll() {
        return repository.findAll();
    }

    public Record save(Record record) {
        return repository.save(record);
    }

    public List<Record> saveAll(List<Record> records) {
        return repository.saveAll(records);
    }

    public void deleteById(UUID id) {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("The record with id: " + id + " was not found");
        }
        repository.deleteById(id);
    }

    public void delete(Record record) {
        repository.delete(record);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

}
