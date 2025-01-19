package ru.itis.fisd.service;

import ru.itis.fisd.mapper.impl.GameHistoryMapper;
import ru.itis.fisd.model.GameHistoryEntity;
import ru.itis.fisd.repository.GameHistoryRepository;

import java.util.List;

public class GameHistoryService {

    private final GameHistoryRepository gameHistoryRepository;

    public GameHistoryService() {
        this.gameHistoryRepository = new GameHistoryRepository(new GameHistoryMapper());
    }

    public List<GameHistoryEntity> getAll() {
        return gameHistoryRepository.getAll();
    }

    public boolean insert(GameHistoryEntity gameHistoryEntity) {
        return gameHistoryRepository.insert(gameHistoryEntity);
    }
}
