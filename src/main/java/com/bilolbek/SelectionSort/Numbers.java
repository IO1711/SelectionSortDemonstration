package com.bilolbek.SelectionSort;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "NumbersTable")
public class Numbers {

    @Id
    @GeneratedValue
    private int Id;

    private int numbers;

    public Numbers() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    @Override
    public String toString() {
        return "Numbers{" +
                "Id=" + Id +
                ", numbers=" + numbers +
                '}';
    }
}
