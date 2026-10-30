# VisualX — Fabric 1.21.11

## Сборка JAR через GitHub Actions (без установки Gradle на свой ПК)

1. Создай репозиторий на https://github.com/new (например, `VisualX`).
2. Распакуй этот ZIP на компьютере.
3. Загрузи **содержимое** папки `VisualX` в репозиторий GitHub (включая скрытую папку `.github`).
4. Открой вкладку **Actions** в репозитории.
5. Если GitHub попросит, нажми **I understand my workflows, go ahead and enable them**.
6. Выбери workflow **Build VisualX JAR** и нажми **Run workflow**. Также сборка стартует при push в `main` или `master`.
7. Дождись зелёной галочки. Открой завершённый запуск, внизу найди **Artifacts** и скачай `VisualX-Fabric-1.21.11`.
8. Распакуй скачанный artifact. Внутри будет `.jar`; положи его в `.minecraft/mods`.

## Требования
- Minecraft Java Edition 1.21.11
- Fabric Loader и Fabric API для 1.21.11
- Java 21

## Локальная сборка
Если Gradle установлен, из корня проекта выполни `gradle build`.
Готовый файл появится в `build/libs/`.

## Примечание
Workflow собирает проект на GitHub Actions. Сам JAR считается готовым только после успешной сборки (зелёная галочка) и скачивания artifact.
