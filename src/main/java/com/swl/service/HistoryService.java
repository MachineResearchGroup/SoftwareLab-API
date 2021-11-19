package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.project.Board;
import com.swl.models.project.Columns;
import com.swl.models.project.History;
import com.swl.models.project.Task;
import com.swl.payload.request.HistoryRequest;
import com.swl.payload.request.TaskRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.repository.ColumnRepository;
import com.swl.repository.HistoryRepository;
import com.swl.repository.TaskRepository;
import com.swl.util.CopyUtil;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HistoryService {

    @Autowired
    private final ColumnRepository columnRepository;

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final HistoryRepository repository;


    public void verifyHistory(HistoryRequest historyRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        History history = new History();

        if (columnRepository.findById(historyRequest.getIdColumn()).isEmpty()) {
            throw new NotFoundException(Columns.class);
        }

        modelUtil.map(historyRequest, history);
        ErrorResponse error = modelUtil.validate(history);

        if (!Objects.isNull(error)) {
            throw new InvalidFieldException(error);
        }

    }


    public History registerHistory(HistoryRequest historyRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Optional<Columns> column = columnRepository.findById(historyRequest.getIdColumn());

        if (column.isPresent()) {
            History history = new History();
            modelUtil.map(historyRequest, history);
            history.setColumn(column.get());

            history = repository.save(history);

            if (Objects.isNull(column.get().getHistories()))
                column.get().setHistories(new ArrayList<>());
            column.get().getHistories().add(history);

            columnRepository.save(column.get());
            return history;
        } else {
            throw new NotFoundException(Columns.class);
        }
    }


    public History editHistory(Integer idHistory, HistoryRequest historyRequest) {
        History historyAux = getHistory(idHistory);
        CopyUtil.copyProperties(historyRequest, historyAux);
        return repository.save(historyAux);
    }


    public History getHistory(Integer idHistory) {
        Optional<History> history = repository.findById(idHistory);
        if (history.isPresent()) {
            return history.get();
        } else {
            throw new NotFoundException(Board.class);
        }
    }


    public List<History> getAllHistoryByColumn(Integer idColumn) {
        Optional<Columns> column = columnRepository.findById(idColumn);
        if (column.isPresent()) {
            return repository.findAllByColumnId(idColumn).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Columns.class);
    }


    public History addTask(Integer idHistory, TaskRequest taskRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        History history = getHistory(idHistory);

        Task task = new Task();
        modelUtil.map(taskRequest, task);

        task = taskRepository.save(task);

        if (Objects.isNull(history.getTasks()))
            history.setTasks(new ArrayList<>());
        history.getTasks().add(task);

        return repository.save(history);

    }


    public void deleteHistory(Integer idHistory) {
        History history = getHistory(idHistory);
        repository.deleteById(history.getId());
    }

}
