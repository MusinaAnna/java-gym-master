package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;
    private final Map<DayOfWeek, List<TrainingSession>> dayCache;

    public Timetable() {
        timetable = new HashMap<>();
        dayCache = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> scheduleDay = timetable.get(day);
        List<TrainingSession> sessions = scheduleDay.get(time);
        if (sessions == null) {
            sessions = new ArrayList<>();
            scheduleDay.put(time, sessions);
        }
        sessions.add(trainingSession);

        dayCache.remove(day);
        //сохраняем занятие в расписании
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (dayCache.containsKey(dayOfWeek)) {
            List<TrainingSession> cached = dayCache.get(dayOfWeek);
            return new ArrayList<>(cached);
        }
        TreeMap<TimeOfDay, List<TrainingSession>> scheduleDay = timetable.get(dayOfWeek);
        List<TrainingSession> result = new ArrayList<>();

        for (List<TrainingSession> sessions : scheduleDay.values()) {
            result.addAll(sessions);
        }

        dayCache.put(dayOfWeek, new ArrayList<>(result));

        return result;
    }

    //как реализовать, тоже непонятно, но сложность должна быть О(1)


    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {

        TreeMap<TimeOfDay, List<TrainingSession>> scheduleDay = timetable.get(dayOfWeek);
        List<TrainingSession> sessions = scheduleDay.get(timeOfDay);

        if (sessions == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(sessions);
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCountMap = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);


            for (List<TrainingSession> sessionsAtTime : daySchedule.values()) {
                for (TrainingSession session : sessionsAtTime) {
                    Coach coach = session.getCoach();
                    int currentCount = coachCountMap.getOrDefault(coach, 0);
                    coachCountMap.put(coach, currentCount + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCountMap.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(result);

        return result;
    }

}
