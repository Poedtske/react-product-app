package com.example.backend.service.impl;

import com.example.backend.model.Event;
import com.example.backend.model.Tafel;
import com.example.backend.repository.TableDao;
import com.example.backend.service.TableService;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TableServiceImpl implements TableService {
    @Autowired
    private TableDao tableRepository;

    @Override
    public Tafel save(Tafel tafel) {
        return tableRepository.save(tafel);
    }

    @Override
    public Tafel updateById(Long id, Tafel tafel) {
        Tafel t=tableRepository.findById(id).orElseThrow(null);
        try{
            t.setSeats(tafel.getSeats());
            return tableRepository.save(t);
        }catch(Exception e){
            throw e;
        }

    }

    @Override
    public Iterable<Tafel> findAll() {
        return tableRepository.findAll();
    }

    @Override
    public Tafel findById(Long id) {
        return tableRepository.findById(id).orElseThrow(null);
    }

    @Override
    public void deleteById(Long id) {
        tableRepository.deleteById(id);
    }

    public void CreateTables(Event e){
        for(int i = e.getTables().size(); i<(e.getKolommen()*e.getRijen()); i++){
            Tafel t=new Tafel(e,e.getSeatsPerTable());
            tableRepository.save(t);
            e.AddTable(t);
        }
    }
    public void CreateTables(Event e, int amount){
        for(int i = 0; i<amount; i++){
            Tafel t=new Tafel(e,e.getSeatsPerTable());
            tableRepository.save(t);
            e.AddTable(t);
        }
    }

    public void RemoveTables(Event e, int amount){
        int arrayLength=e.getTables().size();
        List<Tafel> tables=e.getTables();
        List<Long> idList=new ArrayList<>();
        for(int i=e.getTables().size()-1;i>=arrayLength-amount;i--){
            Long id=tables.get(i).getId();
            idList.add(id);
        }
        tableRepository.deleteAllById(idList);
    }
}
