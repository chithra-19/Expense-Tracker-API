package com.expensetracker.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequestDTO {
	
	@NotBlank
    private String title;

    @NotNull
    @Positive
    private BigDecimal amount;

	@NotNull
    private String category;

    private String description;

    @NotNull
    private LocalDate date;
    
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

    public BigDecimal getAmount() {
		return amount;
	}

}
