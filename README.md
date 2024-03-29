# API tests for https://stellarburgers.nomoreparties.site

In this project I created tests for the study web-site API

Project is developed with:

- Java
- Maven
- JUnit
- RestAssured
- Allure
- ✨Magic ✨


Project contains following classes:

| CLASS | README |
| ------ | ------ |
| main/RestClient | base JSON settings |
| main/BurgerUser | class with constructors for user creation |
| main/UserCredentials | class with constructors for user credentials creation |
| main/BurgersUserMethods | all user methods |
| main/Order | class with order constructor |
| main/OrderMethods | all order methods |
| test/UserCreateTests | positive and negative tests for user creation |
| test/UserLoginTests | positive and negative tests for user login |
| test/UpdateUserDataTests | positive and negative tests to update the user information |
| test/OrderCreateTests | order creation tests with all possible conditions |
| test/GetOrdersTest | order getting tests with and without authorization |
| allure-results | testing results report with allure |
