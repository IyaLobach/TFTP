# TFTP

Реализация TFTP клиента и сервера для удаленного обмена файлами.

## Запуск проекта ##

#### 1. Клонируйте репозиторий: ####

`git clone https://github.com/IyaLobach/TFTP.git`

### 2. Скомпилируйте: ###

`javac -d bin src\com\suai\controller\*.java src\com\suai\model\*`

### 3. Запуск сервера: ###

`java -classpath ./bin com.suai.model.Server`

### 4. Запуск клиента: ###

`java -classpath ./bin com.suai.model.Client`

### 5. Подключение клиента: ###

После запуска клиента необходимо указать данные для подключения, а именно ipAddress localhost и port 69:

![Client Connection](https://github.com/IyaLobach/TFTP/blob/main/images/12.png)

## Инструкции пользователя ##

При подключении будут предложены команды:

1. @load
2. @download
3. @show

#### Команда @load ####

Команда для загрузки файла на сервер. Файл будет извлекаться по умолчанию из папки Client в папку Server.

Для корректной работы необходимо ввести @load fileName.

При указании несуществующего файла в консоли появится сообщение об ошибке, в противном случае файл успешно будет загружен, о чем появится сообщение в консоли.

#### Команда @show ####

Команда для просмотра списка файлов на сервере. Файлы сервера будут по умолчанию храниться в папке Server.

Для корректной работы необходимо ввести @show без аргументов.

При неверно указанной команде в консоли появится сообщение об ошибке, в противном случае в консоль будет выведен список доступных файлов.

#### Команда @download ####

Команда для скачивания файла с сервера. Файл будет помещен по умолчанию в папку Client из папки Server.

Для корректной работы необходимо ввести @load fileName.

При указании несуществующего файла в консоли появится сообщение об ошибке, в противном случае файл успешно будет загружен, о чем появится сообщение в консоли.


## Примеры работы программы: ##

![Client Test](https://github.com/IyaLobach/TFTP/blob/main/images/13.png)

![Client Test](https://github.com/IyaLobach/TFTP/blob/main/images/14.png)

![Client Test](https://github.com/IyaLobach/TFTP/blob/main/images/15.png)

![Client Test](https://github.com/IyaLobach/TFTP/blob/main/images/16.png)

![Client Test](https://github.com/IyaLobach/TFTP/blob/main/images/17.png)
