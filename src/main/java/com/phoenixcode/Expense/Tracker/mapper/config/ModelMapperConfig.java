package com.phoenixcode.Expense.Tracker.mapper.config;

import com.phoenixcode.Expense.Tracker.dto.CreateExpenseRequestDto;
import com.phoenixcode.Expense.Tracker.dto.ExpenseResponseDto;
import com.phoenixcode.Expense.Tracker.entity.Expense;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(CreateExpenseRequestDto.class, Expense.class)
                .addMappings(mapper -> {
                    mapper.skip(Expense::setId);
                    mapper.skip(Expense::setCategory);
                    mapper.skip(Expense::setUser);
                });

        modelMapper.typeMap(Expense.class, ExpenseResponseDto.class)
                .addMappings(mapper -> {
                    mapper.map(expense -> expense.getCategory().getName(), ExpenseResponseDto::setCategory);
                });

        return modelMapper;
    }
}
