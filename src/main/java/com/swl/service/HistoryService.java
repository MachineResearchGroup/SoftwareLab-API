package com.swl.service;

import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Columns;
import com.swl.models.project.History;
import com.swl.models.project.Task;
import com.swl.payload.request.HistoryRequest;
import com.swl.payload.request.TaskRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.ColumnRepository;
import com.swl.repository.HistoryRepository;
import com.swl.repository.TaskRepository;
import com.swl.util.CopyUtil;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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


    public ResponseEntity<?> verifyHistory(HistoryRequest historyRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        History history = new History();

        modelUtil.map(historyRequest, history);
        List<MessageResponse> messageResponses = modelUtil.validate(history);

        if (columnRepository.findById(historyRequest.getIdColumn()).isEmpty()) {
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class));
        }

        if (!messageResponses.isEmpty()) {
            return ResponseEntity.badRequest().body(messageResponses);
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.VALID, History.class));
    }


    public History registerHistory(HistoryRequest historyRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Optional<Columns> column = columnRepository.findById(historyRequest.getIdColumn());

        if (column.isPresent()) {
            History history = new History();
            modelUtil.map(historyRequest, history);
            history.setColumn(column.get());

            history = repository.save(history);

            if(Objects.isNull(column.get().getHistories()))
                column.get().setHistories(new ArrayList<>());
            column.get().getHistories().add(history);

            columnRepository.save(column.get());
            return history;
        } else {
            return null;
        }
    }


    public History addTask(Integer idHistory, TaskRequest taskRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Optional<History> history = repository.findById(idHistory);

        if (history.isPresent()) {
            Task task = new Task();
            modelUtil.map(taskRequest, task);

            task = taskRepository.save(task);

            if(Objects.isNull(history.get().getTasks()))
                history.get().setTasks(new ArrayList<>());
            history.get().getTasks().add(task);

            return repository.save(history.get());
        } else {
            return null;
        }
    }


    public History editHistory(Integer idHistory, HistoryRequest historyRequest) {
        Optional<History> historyAux = repository.findById(idHistory);

        if (historyAux.isPresent()) {
            CopyUtil.copyProperties(historyRequest, historyAux);
            return repository.save(historyAux.get());
        }
        return null;
    }


    public History getHistory(Integer idHistory) {
        return repository.findById(idHistory).orElse(null);
    }


    public List<History> getAllHistoryByColumn(Integer idColumn) {
        return repository.findAllByColumnId(idColumn).orElse(null);
    }


    public boolean deleteHistory(Integer idHistory) {
        if (repository.existsById(idHistory)) {
            repository.deleteById(idHistory);
            return true;
        }
        return false;
    }


}
