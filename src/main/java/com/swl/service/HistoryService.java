package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Collaborator;
import com.swl.models.project.Board;
import com.swl.models.project.Columns;
import com.swl.models.project.History;
import com.swl.models.project.Task;
import com.swl.payload.request.HistoryRequest;
import com.swl.payload.request.TaskRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.ColumnRepository;
import com.swl.repository.HistoryRepository;
import com.swl.repository.TaskRepository;
import com.swl.util.CopyUtil;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
public class HistoryService {

    @Autowired
    private final ColumnRepository columnRepository;

    @Autowired
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final HistoryRepository repository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private void verifyHistory(HistoryRequest historyRequest) {
        formatter = formatter.withLocale(Locale.US);

        ModelUtil modelUtil = ModelUtil.getInstance();
        History history = new History();

        try {
            LocalDate formattedData = LocalDate.parse(historyRequest.getEndDate(), formatter);
        } catch (Exception parseException) {
            throw new InvalidFieldException(ErrorResponse.builder()
                    .key("endDate")
                    .build());
        }

        modelUtil.map(historyRequest, history);
        ErrorResponse error = modelUtil.validate(history);

        if (!Objects.isNull(error)) {
            throw new InvalidFieldException(error);
        }

    }


    public History registerHistory(HistoryRequest historyRequest) {
        verifyHistory(historyRequest);

        LocalDate formattedData = LocalDate.parse(historyRequest.getEndDate(), formatter);
        historyRequest.setEndDate(null);

        Optional<Columns> column = columnRepository.findById(historyRequest.getIdColumn());

        if (column.isPresent()) {
            List<Collaborator> collaborators = collaboratorRepository.findAllById(historyRequest.getIdCollaborators());
            if (!collaborators.isEmpty()) {
                History history = new History();
                CopyUtil.copyProperties(historyRequest, history);

                history.setColumn(column.get());
                history.setEndDate(formattedData);
                history.setCollaborators(collaborators);
                history = repository.save(history);

                if (Objects.isNull(column.get().getHistories()))
                    column.get().setHistories(new ArrayList<>());
                column.get().getHistories().add(history);



                columnRepository.save(column.get());
                return history;
            }else{
                throw new NotFoundException(Collaborator.class);
            }
        } else {
            throw new NotFoundException(Columns.class);
        }
    }


    public History editHistory(Integer idHistory, HistoryRequest historyRequest) {
        verifyHistory(historyRequest);
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


    public List<History> getAllHistoriesByCollaborator(Integer idCollaborator) {
        Optional<Collaborator> collaborator = collaboratorRepository.findById(idCollaborator);
        if (collaborator.isPresent()) {
            return repository.findAllByCollaboratorId(idCollaborator).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Collaborator.class);
    }


    public List<History> getAllHistoriesByCollaboratorActual() {
        if (userService.getCurrentUser().isPresent() && userService.getCurrentUser().get() instanceof Collaborator) {
            return getAllHistoriesByCollaborator(((Collaborator) userService.getCurrentUser().get()).getId());
        }
        throw new NotFoundException(Collaborator.class);
    }


    public void deleteHistory(Integer idHistory) {
        History history = getHistory(idHistory);
        Optional<Columns> columns = columnRepository.findByHistoryId(history.getId());
        if (columns.isPresent()) {
            columns.get().getHistories().remove(history);
            columnRepository.save(columns.get());
        }
        repository.deleteById(history.getId());
    }

}
