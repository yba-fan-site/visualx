# VisualX — Fabric 1.21.11

## Что добавлено
- Меню VisualX открывается клавишей Right Shift.
- Клавиша отображается в Options → Controls → Key Binds, категория VisualX.
- В меню можно включать и выключать Branding, FPS, Health & Armor, Coordinates и Keystrokes.
- Меню не ставит игру на паузу.

## Сборка JAR через GitHub Actions
1. Распакуй архив проекта.
2. Загрузи содержимое папки проекта в свой GitHub-репозиторий (включая `.github`).
3. Открой вкладку Actions и запусти workflow **Build VisualX JAR**.
4. Дождись успешной сборки (зелёная галочка), открой запуск и скачай artifact `VisualX-JAR`.
5. Распакуй artifact и положи файл `visualx-lunar-1.0.0.jar` в `.minecraft/mods`.

## Требования
- Minecraft Java Edition 1.21.11
- Fabric Loader 0.18.1 или новее
- Fabric API для 1.21.11
- Java 21

Важно: этот исходный архив содержит изменения меню, но JAR ещё не собран и не проверен запуском в игре. Сначала дождись успешной сборки GitHub Actions.
