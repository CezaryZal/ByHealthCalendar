package com.CezaryZal.day;

import com.CezaryZal.bodySize.BodySizeService;
import com.CezaryZal.day.shortDay.ShortDay;
import com.CezaryZal.day.shortDay.ShortDayService;
import com.CezaryZal.meal.MealService;
import com.CezaryZal.meal.DailyDietDTO;
import com.CezaryZal.note.NoteService;
import com.CezaryZal.training.TrainingService;
import com.CezaryZal.user.User;
import com.CezaryZal.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
@Service
public class DayService {

    private DayRepository dayR;
    private UserService userS;
    private BodySizeService bodySizeS;
    private MealService mealS;
    private TrainingService trainingS;
    private NoteService noteS;
    private ShortDayService shortDayS;


    //Posiadając obecne endpointy możliwe jest stworzenie strony bieżaco doładowując dane
    @Autowired
    public DayService(DayRepository dayR, UserService userS, BodySizeService bodySizeS, MealService mealS,
                      TrainingService trainingS, NoteService noteS, ShortDayService shortDayS) {
        this.dayR = dayR;
        this.userS = userS;
        this.bodySizeS = bodySizeS;
        this.mealS = mealS;
        this.trainingS = trainingS;
        this.noteS = noteS;
        this.shortDayS = shortDayS;
    }

    public Day getDayById(int id){
        Day day = dayR.findById(id);

        return day;
    }

    public DayDTO getDayDTOByDateAndUserId(String inputDate, int userId) {
        Day day = dayR.findDayByDateAndUserId(LocalDate.parse(inputDate), userId);
        User user = userS.getUserById(userId);
        DailyDietDTO dailyDietDTO = mealS.getDailyDietDTO(day.getId());

        return new DayDTO(day.getId(),
                day.getDate(),
                userId,
                user.getNick(),
                bodySizeS.getDateLastMeasureByUserId(userId),
                day.getPortionsDrink(),
                checkIsAchievedDrink(user, day),
                day.getPortionsAlcohol(),
                dailyDietDTO,
                checkIsAchievedKcal(user, dailyDietDTO),
                day.getPortionsSnack(),
                trainingS.createAllTrainingsDTOByDay(day.getListTrainingsDB()),
                noteS.getHeadersByNotesDB(day.getListNotesDB()),
                shortDayS.getShortDaysByDateAndUserId(inputDate, userId)
        );
    }

    public int getDayIdByDateAndUserId(String inputDate, int userId){
        int dayId = dayR.findDayIdByDateAndUserId(LocalDate.parse(inputDate), userId);

        return dayId;
    }

    public Day getDayDBByDateAndUserId(String inputDate, int userId){
        Day day = dayR.findDayByDateAndUserId(LocalDate.parse(inputDate), userId);

        return day;
    }

    public List<Day> getAll (){
        List<Day> listDay = dayR.getAll();

        return listDay;
    }

    public boolean addDay (Day day){
        dayR.save(day);
        shortDayS.addShortDay(createShortDayByDay(day));

        return true;
    }

    public boolean updateDay (Day day){
        dayR.update(day);
        ShortDay shortDay = createShortDayByDay(day);
        shortDay.setId(day.getId());
        shortDayS.updateShortDay(shortDay);
        return true;
    }

    public String deleteDayById(int id){
        Day day = dayR.findById(id);
        if(dayR.delete(day)){
            return "delete record";
        }
        return "Day id not found";
    }

    public boolean checkIsAchievedDrink(User user, Day day){
        return user.getDailyLimits().getDrinkDemand()<= day.getPortionsDrink() * 250;
    }

    public boolean checkIsAchievedKcal(User user, DailyDietDTO dailyDietDTO){
        int kcalDemand = user.getDailyLimits().getKcalDemand();
        int sumOfKcal = dailyDietDTO.getSumOfKcal();
        return sumOfKcal>=kcalDemand-kcalDemand*0.05 && sumOfKcal<=kcalDemand+kcalDemand*0.05;
    }

    public ShortDay createShortDayByDay(Day day){
        User user = userS.getUserById(day.getUserId());
        DailyDietDTO dailyDietDTO = mealS.getDailyDietDTO(day.getId());

        return new ShortDay(
                day.getUserId(),
                day.getDate(),
                checkIsAchievedKcal(user, dailyDietDTO),
                checkIsAchievedDrink(user, day),
                day.getPortionsAlcohol()!=0,
                day.getPortionsSnack()!=0
        );
    }

}
