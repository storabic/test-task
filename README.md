Эта репа содержит две программы:
1. sorter - папка с проектом Intelij Idea и программой под Java 11 для сортировки крупных текстовых файлов.
2. sample-generator.py - Программа на Python 3.9.0 для создания крупных текстовых файлов - сэмплов для тестирования программы 1.
+ файлы для запуска этих программ

Как запускать:
1. Сгенерировать сэмпл: python3 sample_generator.py <N> <M>
N и M - обязательные аргументы, N - количество строк в файле с тестовыми данными, M - максимальная длина строки в тестовом файле.
2. Обработать текстовый файл: java -jar sorter.jar <имя исходного файла> <куда записать отсортированные данные>

Ways to improve:
1. Test that merge sort is better than other sorting algorithms for sorting chunks and rework that moment, if needed.
2. Dynamically set chunk size (with current chunk size, it's not guaranteed that one chunk will fit in every CPU), it should probably depend on given file size?
3. In sample generator N and M - command line arguments should have restrictions.
