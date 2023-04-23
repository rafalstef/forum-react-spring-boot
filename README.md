## Full Stack Reddit-clone with Spring Boot, MySQL and React

### Steps to setup the Spring Boot Backend app

1. **Clone the application and install dependencies**

   ```bash
   git clone https://github.com/rafalstef/forum-react-spring-boot.git
   cd forum-react-spring-boot
   mvn install
   ```

2. **Create MySQL database**

   ```bash
   create database forum
   ```

3. **Change MySQL username and password as per your MySQL installation**

   - create `"/src/main/resources/application-dev.properties"` file.

   - change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation

4. **Create fake SMTP server**

   - create fake SMTP server eg. using mailtrap.io
   - in `"/src/main/resources/application-dev.properties"` change `spring.mail` properties

5. **Run the app**

   You can run the spring boot app by typing the following command

   ```bash
   mvn spring-boot:run
   ```

   The server will start on port 8080.

### Steps to setup the React Frontend app

Go to the `frontend` folder

```bash
cd frontend
```

Then type the following command to install the dependencies

```bash
npm install
```

Start the application

```bash
npm run dev
```

The front-end server will start on port `3000`
