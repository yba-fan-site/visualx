# VisualX — исправление вылета меню (Minecraft 1.21.11)

В этой версии убран вызов `renderBackground(...)` из экрана настроек. В Minecraft 1.21.11 он может запускать blur-проход, когда другой экранный код уже запросил размытие, что приводило к `IllegalStateException: Can only blur once per frame`.

Вместо размытия используется полупрозрачное затемнение фона. Клавиша открытия меню — Right Shift.

## Сборка
1. Загрузите содержимое папки `VisualX-fixed` в корень GitHub-репозитория, сохранив структуру `.github`, `src`, `build.gradle`, `gradle.properties`, `settings.gradle`.
2. Запустите workflow GitHub Actions.
3. Скачайте JAR из artifact после успешной сборки.

Это исправление адресует конкретную ошибку из crash report. Оно не является подтверждением теста в запущенном Minecraft; после сборки проверьте меню в игре.
