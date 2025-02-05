Compiler = javac
Runner = java


Modules = javafx.controls
Path_to_javaFX = /home/kuraterut/Desktop/HSE/my_projects/JavaProjects/javafx-sdk-22.0.2/lib
JDBC_DRIVER = ./client/sources/mysql-connector-java-9.0.0.jar
Main_class = src.Main
Bin_dir = ./client/bin

Files := $(shell find client/src -name '*.java')

CSS = client/style
Mail_JAR = ./client/sources/javax.mail-1.6.2.jar
Activation_JAR = ./client/sources/activation.jar
Json_JAR = ./client/sources/json-simple-1.1.1.jar



# Use ANSI color codes:
BRED    = \033[1;31m
BGREEN  = \033[1;32m
BYELLOW = \033[1;33m
GREEN   = \033[1;35m
BCYAN   = \033[1;36m
RESET   = \033[0m



default: $(Main_class)

# Link all object files together to obtain a binary:
# NOTE: all object files will be built first.
$(Main_class): $(Files) Makefile
	@mkdir -p $(Bin_dir)
	$(Compiler) --module-path $(Path_to_javaFX) --add-modules=$(Modules) -cp $(Json_JAR) -d $(Bin_dir) $(Files)

run: $(Main_class) Makefile
	$(Runner) --module-path $(Path_to_javaFX) --add-modules=$(Modules) -classpath $(Bin_dir):$(CSS):$(Json_JAR) $(Main_class)

tutorial:
	@sudo apt update
	@sudo apt install openjdk-17-jdk
	@printf "\n\nНеобходимые пакеты java установлены\n\n"
	@printf "Теперь скачай с сайта https://gluonhq.com/products/javafx/ модули javafx со следующими параметрами:\n\n"
	@printf "Версия javaFX - 22.0.2, Операционка Linux, Архитектура x64, тип SDK\n\n"
	@printf "Далее распакуй все в отдельную папку рядом с репозиторием(Именно рядом, не внутри).\n\n"
	@printf "Затем в своем Makefile укажи путь к модулям в переменной Path_to_javaFX, в следующей форме: /home/<Имя пользователя>/.../javafx-sdk-22.0.2/lib\n\n"
	@printf "Затем попробуй собрать и запустить проект у себя на компьютере, для этого зайди в директорию VeronichkaNails_APP и напиши в командной строке make run\n\n"


clean:
	@printf "$(BYELLOW)Cleaning build directory $(RESET)\n"
	rm -rf $(Bin_dir)

# List of non-file targets:
.PHONY: run clean default
	

