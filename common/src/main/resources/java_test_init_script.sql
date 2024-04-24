INSERT INTO test (title, description, specialty) VALUES
    ('Java test1', 'Тест по основам Java', 'Java_developer');

INSERT INTO question (question_txt, question_num) VALUES
                                                      ('класс', 1),
                                                      ('основные принципы ооп', 2),
                                                      ('верно ли, что наследование может быть одиночным и множественным', 3),
                                                      ('Чтобы объявить один класс наследником от другого, надо использовать после имени класса-наследника ключевое слово extends, после которого идет имя базового класса.', 4),
                                                      ('Статические методы нельзя переопределить, можно только перегрузить', 5),
                                                      ('Аннотация переопределения метода.', 6),
                                                      ('Перегрузка методов', 7),
                                                      ('Теория ООП выделяет отношения (связи) между классами:', 8),
                                                      ('Агрегация и композиция являются частными случаями ассоциации', 9),
                                                      ('Композиция', 10);

INSERT INTO test_questions (test_id, question_id) VALUES
                                                      (1, 1),
                                                      (1, 2),
                                                      (1, 3),
                                                      (1, 4),
                                                      (1, 5),
                                                      (1, 6),
                                                      (1, 7),
                                                      (1, 8),
                                                      (1, 9),
                                                      (1, 10);

-- Вставка ответов
-- Для простоты добавим все ответы, и пометим правильные ответы в соответствии с указанным форматом
-- Для ответов с isCorrect = true добавим текста ' - **' в конце
INSERT INTO answer (answer_txt, answer_num, is_correct, question_id) VALUES
                                                                         ('это описание ещё не созданного объекта, общий шаблон.', 1, true, 1),
                                                                         ('экземпляр, созданный по шаблону с собственным состоянием свойств.', 2, false, 1),
                                                                         ('методология программирования, основной концепцией является понятие объекта', 3, false, 1),
                                                                         ('полиморфизм, инкапсуляция, наследование', 1, true, 2),
                                                                         ('полиморфизм, инверсия, инкапсуляция', 2, false, 2),
                                                                         ('наследование, модификация, абстракция', 3, false, 2),
                                                                         ('да', 1, true, 3),
                                                                         ('нет', 2, false, 3),
                                                                         ('да', 1, true, 4),
                                                                         ('нет', 2, false, 4),
                                                                         ('да', 1, true, 5),
                                                                         ('нет', 2, false, 5),
                                                                         ('@Override', 1, true, 6),
                                                                         ('@Overload', 2, false, 6),
                                                                         ('@Overrun', 3, false, 6),
                                                                         ('Java разрешает определение внутри одного класса двух или более методов с одним именем, если только объявления их параметров различны.', 1, true, 7),
                                                                         ('позволяет взять какой-то метод родительского класса и написать в каждом классе-наследнике свою специфическую реализацию этого метода. Новая реализация «заменит» родительскую в дочернем классе.', 2, false, 7),
                                                                         ('наследование, ассоциация', 1, true, 8),
                                                                         ('наследование, связывание', 2, false, 8),
                                                                         ('ассоциация, подчинение', 3, false, 8),
                                                                         ('да', 1, true, 9),
                                                                         ('нет', 2, false, 9),
                                                                         ('это более жёсткое отношение, когда объект не только является частью другого объекта, но и вообще не может принадлежат кому-то.', 1, true, 10),
                                                                         ('отношение, когда один объект является частью другого.', 2, false, 10);

-- Вставка правильных ответов в таблицу right_answers
INSERT INTO right_answers (test_id, answer_id) VALUES
                                                   (1, 1),
                                                   (1, 4),
                                                   (1, 7),
                                                   (1, 9),
                                                   (1, 11),
                                                   (1, 13),
                                                   (1, 16),
                                                   (1, 18),
                                                   (1, 21),
                                                   (1,23);