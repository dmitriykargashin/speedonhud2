# Хронология разработки проекта SpeedOnHud2.0 #

15 мин: поиск примера работы с GPS
30 мин: разбор примера работы с GPS
10 мин: создание проекта SpeedOnHud
10 мин: ввод примера работы с GPS вл проект
20 мин: поиск решения, почему не работает override - в Compiler Java поставить минимум 1.6
30 мин: запуск виртуального девайса, компиляция, посылка координат виртуальному девайсу через DDMS - работает ;)
05 мин: загрузка пакета на реальный девайс
20 мин: попытка поймать спутники - не вышло... в окно не ловит.
20 мин: поймал спутники на улице, скорость показывает странную
15 мин: выяснил, что скорость провайдер выдаёт в м/сек, а не км/час.
40 мин: читал как выводить графику на Canvas.
10 мин: перевел вывод значения скорости в км/час
05 мин: сделал подсчёт пройденного расстояния
60 мин: подумал про OpenGL. Почитал примеры, сравнения с Canvas. Вывод: буду выводить графику в OpenGL, сложнее, но скорость выше
10 мин: ввел в проект работу с OpenGL - на эмуляторе моём не работает оказывается, GPU не включается пишет про библиотеку Intel .... - нет её. На реальном девайсе работает.
20 мин: Тестирование на улице. Дистанция слишком быстро нарастает. Т.к. каждое изменение координат происходит приращение плюс погрешности приёмника, поэтому надо сделать интервал опроса изменения координат конкретным и побольше - 0.5 сек.Или как-то интерполировать траекторию...
20 мин: установил эмулятор genymotion - теперь OpenGL работает на этом эмуляторе
180 мин: Разбор примеров работы с OpenGL
120 мин: Изменение примеров работы с OpenGL с сайта http://developer.android.com/
120 мин: Пример на эмуляторе работает, а на реальном девайсе нет! Предположительно это утечки памяти.
120 мин: другие примеры работают без проблем. 
180 мин: связывание показаний GPS с объектами openGL.  Всё получилось. Связь есть.
60 мин: поиск решения для вывода текста (цифр) на glsurface. Найдено. Работает. 
60 мин: размышления как хранить и рисовать объекты. Решено нарисовать объекты в графическом редакторе и выгрузить в формат с вершинами. Хранить буду эти простые объекты пока прямо в коде.
60 мин: рисование шкалы в inkscape и выгрузка в подходящий формат. В Tex удобно.
20 мин: Создание макета интерфейса.
60 мин: отображение шкалы в приложении 
180 мин: осознавание как размещать, перемещать и вращать openGL объекты. Чтение и разбор примеров. 
90 мин: анализ алгоритмов интерполяции показаний датчиков . Написал свое подобие скользящей средней. 
60 мин: отображение шкалы с подвижной стрелкой
120 мин: завязывание показаний скорости и стрелки. Тестирование.  Работает
120 мин: как же так получилось, что в OpenGL нет поддержки вывода текста? Разбираюсь как вывести текст с помощью текстуры. 
40 мин. Не загружается шрифт. Отладка приложения. Оказалось папка assets не там расположена. 
30 мин. Одновременно шкала и текст не показываются. Надо разбираться с матрицами проекции. 
90 мин. Шкала и текст теперь показываются одновременно. 
180 мин. Создание активити с настройками, сохранение параметров при повороте экрана, нормальная реакция на закрытие активити с настройками. 
120 мин. Передача параметров для отображения в указанном режиме. 
120 мин. Пытаюсь зеркально отобразить текст. Переделываю класс Gltext для генерации зеркальной текстуры текста. 
100 мин.  Переделал для возможности вывода зеркального текста. 
100 мин. Размещение текста для нормального отображения в режиме на стекло
40 мин. Поддержка пропорционального размера шрифта в разных разрешениях
60 мин. Поддержка режима блокировки выключения экрана
40 мин. Рисование иконки и подключение в приложение
30 мин. Проверка исходников, чистка.

Итого затраты времени: 2840 мин = 47 часов.