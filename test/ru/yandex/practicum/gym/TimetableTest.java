package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.List;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size(), "Должно быть 1 занятие в понедельник");
        Assertions.assertEquals(singleTrainingSession, mondaySessions.get(0),
                "Занятие должно совпадать с добавленным");

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(), "Во вторник не должно быть занятий");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size(),
                "Должно быть 1 занятие в понедельник");
        Assertions.assertEquals(mondayChildTrainingSession, mondaySessions.get(0),
                "Занятие должно быть детской акробатикой в 13:00");

        // Проверить, что за четверг вернулось два занятия в правильном порядке:сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size(),
                "Должно быть 2 занятия в четверг");

        TrainingSession firstSession = thursdaySessions.get(0);
        TrainingSession secondSession = thursdaySessions.get(1);

        Assertions.assertEquals(thursdayChildTrainingSession, firstSession,
                "Первое занятие должно быть в 13:00 (детская акробатика)");
        Assertions.assertEquals(thursdayAdultTrainingSession, secondSession,
                "Второе занятие должно быть в 20:00 (взрослая акробатика)");

        // Дополнительная проверка времени
        Assertions.assertEquals(13, firstSession.getTimeOfDay().getHours(),
                "Первое занятие должно быть в 13 часов");
        Assertions.assertEquals(0, firstSession.getTimeOfDay().getMinutes(),
                "Первое занятие должно быть в 0 минут");
        Assertions.assertEquals(20, secondSession.getTimeOfDay().getHours(),
                "Второе занятие должно быть в 20 часов");
        Assertions.assertEquals(0, secondSession.getTimeOfDay().getMinutes(),
                "Второе занятие должно быть в 0 минут");

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(),
                "Во вторник не должно быть занятий");

        // Проверить, что за субботу вернулось одно занятие
        List<TrainingSession> saturdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.SATURDAY);
        Assertions.assertEquals(1, saturdaySessions.size(),
                "Должно быть 1 занятие в субботу");
        Assertions.assertEquals(saturdayChildTrainingSession, saturdaySessions.get(0),
                "Занятие должно быть детской акробатикой в 10:00");
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> sessionsAt1300 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertEquals(1, sessionsAt1300.size(),
                "Должно быть 1 занятие в понедельник в 13:00");
        Assertions.assertEquals(singleTrainingSession, sessionsAt1300.get(0),
                "Занятие должно совпадать с добавленным");

        // Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> sessionsAt1400 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(sessionsAt1400.isEmpty(),
                "Не должно быть занятий в понедельник в 14:00");

        // Дополнительные проверки для другого времени
        List<TrainingSession> sessionsAt1200 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));
        assertTrue(sessionsAt1200.isEmpty(),
                "Не должно быть занятий в понедельник в 12:00");

        List<TrainingSession> sessionsAt1300Tuesday = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0));
        assertTrue(sessionsAt1300Tuesday.isEmpty(),
                "Не должно быть занятий во вторник в 13:00");
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();


        // Проверяем, что для всех дней недели возвращаются пустые списки
        for (DayOfWeek day : DayOfWeek.values()) {
            List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(day);
            assertTrue(sessions.isEmpty(),
                    "Для пустого расписания список занятий должен быть пустым для дня: " + day);
        }

        // Проверяем, что для любого времени в любой день недели возвращается пустой список
        TimeOfDay[] testTimes = {
                new TimeOfDay(0, 0),
                new TimeOfDay(9, 0),
                new TimeOfDay(12, 30),
                new TimeOfDay(18, 45),
                new TimeOfDay(23, 59)
        };

        for (DayOfWeek day : DayOfWeek.values()) {
            for (TimeOfDay time : testTimes) {
                List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(day, time);
                assertTrue(sessions.isEmpty(),
                        "Для пустого расписания не должно быть занятий в " + day + " в " +
                                time.getHours() + ":" + time.getMinutes());
            }
        }
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петрова", "Мария", "Сергеевна");

        Group group1 = new Group("Акробатика", Age.CHILD, 60);
        Group group2 = new Group("Йога", Age.ADULT, 90);
        Group group3 = new Group("Стретчинг", Age.ADULT, 45);

        // Создаем 3 тренировки в одно и то же время
        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession session3 = new TrainingSession(group3, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));


        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);


        // Проверяем, что в среду есть 3 занятия
        List<TrainingSession> wednesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY);
        Assertions.assertEquals(3, wednesdaySessions.size(),
                "Должно быть 3 занятия в среду");

        // Проверяем, что в 18:00 есть 3 занятия
        List<TrainingSession> sessionsAt1800 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        Assertions.assertEquals(3, sessionsAt1800.size(),
                "Должно быть 3 занятия в среду в 18:00");

        // Проверяем, что все добавленные занятия присутствуют
        assertTrue(sessionsAt1800.contains(session1),
                "Список должен содержать session1");
        assertTrue(sessionsAt1800.contains(session2),
                "Список должен содержать session2");
        assertTrue(sessionsAt1800.contains(session3),
                "Список должен содержать session3");
    }

    @Test
    void testOrderingAcrossDifferentTimes() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Сидоров", "Алексей", "Владимирович");
        Group group = new Group("Гимнастика", Age.ADULT, 60);

        // Добавляем тренировки в разном порядке
        TrainingSession session1 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0));
        TrainingSession session2 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(9, 0));
        TrainingSession session3 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(15, 30));
        TrainingSession session4 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(12, 0));

        // добавляем в произвольном порядке
        timetable.addNewTrainingSession(session1); // 20:00
        timetable.addNewTrainingSession(session2); // 9:00
        timetable.addNewTrainingSession(session3); // 15:30
        timetable.addNewTrainingSession(session4); // 12:00


        List<TrainingSession> fridaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY);
        Assertions.assertEquals(4, fridaySessions.size(),
                "Должно быть 4 занятия в пятницу");

        // Проверяем порядок: должен быть отсортирован по времени
        // 9:00 → 12:00 → 15:30 → 20:00
        Assertions.assertEquals(session2, fridaySessions.get(0), // 9:00
                "Первое занятие должно быть в 9:00");
        Assertions.assertEquals(session4, fridaySessions.get(1), // 12:00
                "Второе занятие должно быть в 12:00");
        Assertions.assertEquals(session3, fridaySessions.get(2), // 15:30
                "Третье занятие должно быть в 15:30");
        Assertions.assertEquals(session1, fridaySessions.get(3), // 20:00
                "Четвертое занятие должно быть в 20:00");
    }


    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Акробатика", Age.CHILD, 60);

        // Добавляем 3 тренировки для одного тренера
        timetable.addNewTrainingSession(new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coach, DayOfWeek.FRIDAY, new TimeOfDay(18, 0)));


        List<CounterOfTrainings> result = timetable.getCountByCoaches();


        assertEquals(1, result.size(), "Должен быть 1 тренер в списке");

        CounterOfTrainings counter = result.get(0);
        assertEquals(coach, counter.getCoach(), "Тренер должен совпадать");
        assertEquals(3, counter.getCount(), "У тренера должно быть 3 тренировки");
    }

    @Test
    void testGetCountByCoachesMultipleCoachesSorted() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");  // 4 тренировки
        Coach coach2 = new Coach("Петрова", "Мария", "Сергеевна"); // 2 тренировки
        Coach coach3 = new Coach("Сидоров", "Алексей", "Владимирович"); // 1 тренировка

        Group group = new Group("Акробатика", Age.CHILD, 60);

        // Добавляем тренировки в разном порядке для 1  тренера
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.THURSDAY, new TimeOfDay(12, 0)));

        // 2 тренировки для 2-го тренера
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.FRIDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.SATURDAY, new TimeOfDay(14, 0)));

        //  1 тренировка для 3-го
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.SUNDAY, new TimeOfDay(15, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(3, result.size(), "Должно быть 3 тренера в списке");

        // Проверяем сортировку по убыванию количества тренировок
        CounterOfTrainings first = result.get(0);
        CounterOfTrainings second = result.get(1);
        CounterOfTrainings third = result.get(2);

        assertEquals(coach1, first.getCoach(), "Первый должен быть тренер с наибольшим количеством тренировок");
        assertEquals(4, first.getCount(), "У первого тренера должно быть 4 тренировки");

        assertEquals(coach2, second.getCoach(), "Второй должен быть тренер со средним количеством тренировок");
        assertEquals(2, second.getCount(), "У второго тренера должно быть 2 тренировки");

        assertEquals(coach3, third.getCoach(), "Третий должен быть тренер с наименьшим количеством тренировок");
        assertEquals(1, third.getCount(), "У третьего тренера должно быть 1 тренировка");

        // Проверяем, что список отсортирован по убыванию
        assertTrue(first.getCount() >= second.getCount(),
                "Количество тренировок должно идти по убыванию");
        assertTrue(second.getCount() >= third.getCount(),
                "Количество тренировок должно идти по убыванию");
    }

    @Test
    void testGetCountByCoachesSameCount() {
        // ARRANGE
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петрова", "Мария", "Сергеевна");

        Group group1 = new Group("Акробатика", Age.CHILD, 60);
        Group group2 = new Group("Йога", Age.ADULT, 90);

        // У обоих тренеров по 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group1, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group1, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group2, coach2, DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach2, DayOfWeek.THURSDAY, new TimeOfDay(15, 0)));


        List<CounterOfTrainings> result = timetable.getCountByCoaches();


        assertEquals(2, result.size(), "Должно быть 2 тренера в списке");

        // Проверяем, что у обоих по 2 тренировки
        assertEquals(2, result.get(0).getCount(), "У первого тренера должно быть 2 тренировки");
        assertEquals(2, result.get(1).getCount(), "У второго тренера должно быть 2 тренировки");

        // Проверяем, что оба тренера присутствуют (порядок не важен при равенстве)
        List<Coach> coaches = List.of(result.get(0).getCoach(), result.get(1).getCoach());
        assertTrue(coaches.contains(coach1), "Список должен содержать coach1");
        assertTrue(coaches.contains(coach2), "Список должен содержать coach2");
    }

    @Test
    void testGetCountByCoachesWithMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group1 = new Group("Акробатика", Age.CHILD, 60);
        Group group2 = new Group("Йога", Age.ADULT, 90);
        Group group3 = new Group("Стретчинг", Age.ADULT, 45);

        // Один тренер ведет 3 разные группы в одно и то же время
        timetable.addNewTrainingSession(new TrainingSession(
                group1, coach, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group2, coach, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group3, coach, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));


        List<CounterOfTrainings> result = timetable.getCountByCoaches();


        assertEquals(1, result.size(), "Должен быть 1 тренер в списке");
        assertEquals(3, result.get(0).getCount(),
                "У тренера должно быть 3 тренировки (даже если они в одно время)");
    }
}
