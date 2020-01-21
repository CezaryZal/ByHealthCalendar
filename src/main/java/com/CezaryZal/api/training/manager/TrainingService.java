package com.CezaryZal.api.training.manager;

import com.CezaryZal.api.training.manager.mapper.TrainingConverter;
import com.CezaryZal.api.training.model.TrainingDto;
import com.CezaryZal.api.training.model.entity.Training;
import com.CezaryZal.api.training.model.TrainingsSummary;
import com.CezaryZal.api.training.manager.creator.TrainingsSummaryCreator;
import com.CezaryZal.api.training.manager.repo.TrainingRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {

    private final TrainingRepoService trainingRepoService;
    private final TrainingConverter trainingConverter;
    private final TrainingsSummaryCreator trainingsSummaryCreator;

    @Autowired
    public TrainingService(TrainingRepoService trainingRepoService,
                           TrainingConverter trainingConverter,
                           TrainingsSummaryCreator trainingsSummaryCreator) {
        this.trainingRepoService = trainingRepoService;
        this.trainingConverter = trainingConverter;
        this.trainingsSummaryCreator = trainingsSummaryCreator;
    }

    public TrainingDto getTrainingDtoById (Long id){
        return trainingConverter.mappingTrainingToDto(trainingRepoService.getTrainingById(id));
    }

    public TrainingsSummary getTrainingsSummaryByDayId (Long dayId){
        return getTrainingsSummaryByTrainings(trainingRepoService.getTrainingsByDayId(dayId));
    }

    public TrainingsSummary getTrainingsSummaryByTrainings(List<Training> trainingsByDayId){
        List<TrainingDto> trainingsDto = trainingConverter.mappingListTrainingToListDto(trainingsByDayId);
        return trainingsSummaryCreator.createTrainingsSummary(trainingsDto);
    }

    public List<TrainingDto> getAllTrainingsDto (){
        return trainingConverter.mappingListTrainingToListDto(trainingRepoService.getAllTrainings());
    }

    public String addTrainingByDto (TrainingDto trainingDto){
        trainingRepoService.addTraining(trainingConverter.mappingDtoToTraining(trainingDto));
        return "Przesłany trening został zapisany w bazie danych";
    }

    public String updateTrainingByDto (TrainingDto trainingDto){
        trainingRepoService.updateTraining(trainingConverter.mappingDtoToTraining(trainingDto));
        return "Przesłany trening został uaktualniony";
    }

    public String deleteTraining (Long id){
        trainingRepoService.deleteTrainingById(id);
        return "Trening o przesłanym id została usuniety";
    }
}
