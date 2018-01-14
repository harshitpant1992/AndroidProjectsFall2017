package com.example.mpant.common;

interface ServiceInterface {
    List monthlyCash(int year);
    List dailyCash(int day,int month, int year, int numberOfWorkingDays);
    int yearlyAvg(int year);
}