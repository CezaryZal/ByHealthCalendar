package com.CezaryZal.api.meal.manager.mapper;

import com.CezaryZal.api.meal.model.entity.Meal;
import com.CezaryZal.api.meal.model.MealDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealConverter {

    public MealDto mappingMealToDto(Meal meal){
        return MealDto.Builder.builder()
                .id(meal.getId())
                .dateTimeOfEat(meal.getDateTimeOfEat())
                .type(meal.getType())
                .kcal(meal.getKcal())
                .description(meal.getDescription())
                .dayId(meal.getDayId())
                .build();
    }

    public Meal mappingDtoToMeal(MealDto mealDto){
        return Meal.Builder.builder()
                .id(mealDto.getId())
                .dateTimeOfEat(mealDto.getDateTimeOfEat())
                .type(mealDto.getType())
                .kcal(mealDto.getKcal())
                .description(mealDto.getDescription())
                .dayId(mealDto.getDayId())
                .build();
    }

    public List<MealDto> mappingListMealToListDto(List<Meal> meals){
        return meals.stream()
                .map(this::mappingMealToDto)
                .collect(Collectors.toList());
    }
}
