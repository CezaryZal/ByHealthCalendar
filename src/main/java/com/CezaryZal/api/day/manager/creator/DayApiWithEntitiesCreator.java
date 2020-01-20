package com.CezaryZal.api.day.manager.creator;

import com.CezaryZal.api.body.manager.repo.BodySizeRepoService;
import com.CezaryZal.api.day.entity.api.DayApiWithConnectedEntities;
import com.CezaryZal.api.day.entity.day.Day;
import com.CezaryZal.api.limits.manager.checker.LimitsChecker;
import com.CezaryZal.api.meal.entity.DailyDiet;
import com.CezaryZal.api.meal.manager.MealService;
import com.CezaryZal.api.note.manager.creator.HeadersCreator;
import com.CezaryZal.api.shortReport.manager.ShortReportService;
import com.CezaryZal.api.training.manager.TrainingService;
import com.CezaryZal.api.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DayApiWithEntitiesCreator {

    private final LimitsChecker limitsChecker;
    private final MealService mealService;
    private final BodySizeRepoService bodySizeRepoService;
    private final TrainingService trainingService;
    private final HeadersCreator headersCreator;
    private final ShortReportService shortReportService;

    @Autowired
    public DayApiWithEntitiesCreator(LimitsChecker limitsChecker,
                                     MealService mealService,
                                     BodySizeRepoService bodySizeRepoService,
                                     TrainingService trainingService,
                                     HeadersCreator headersCreator,
                                     ShortReportService shortReportService) {
        this.limitsChecker = limitsChecker;
        this.mealService = mealService;
        this.bodySizeRepoService = bodySizeRepoService;
        this.trainingService = trainingService;
        this.headersCreator = headersCreator;
        this.shortReportService = shortReportService;
    }

    public DayApiWithConnectedEntities createByDayAndUser (Day day, User user){
        int sumOfKcal = mealService.getDailyDietByListMeal(day.getListMealsDB()).getSumOfKcal();
        LocalDate dateLastMeasureBody = bodySizeRepoService.getDateLastMeasureByUserId(user.getId());
        DailyDiet dailyDietByListMeal = mealService.getDailyDietByListMeal(day.getListMealsDB());

        return new DayApiWithConnectedEntities(
                day.getId(),
                day.getDate(),
                user.getId(),
                day.getPortionsDrink(),
                day.getPortionsAlcohol(),
                day.getPortionsSnack(),
                user.getNick(),
                dateLastMeasureBody,
                limitsChecker.checkIsAchievedDrink(
                        user.getDailyLimits().getDrinkDemandPerDay(),
                        day.getPortionsDrink()),
                dailyDietByListMeal,
                limitsChecker.checkIsAchievedKcal(
                        user.getDailyLimits().getKcalDemandPerDay(),
                        sumOfKcal),
                trainingService.getTrainingsSummaryByTrainings(day.getListTrainingsDB()),
                headersCreator.getHeadersByNotes(day.getListNotesDB()),
                shortReportService.getShortReportsByDateAndUserId(day.getDate(), user.getId())
        );
    }
}
