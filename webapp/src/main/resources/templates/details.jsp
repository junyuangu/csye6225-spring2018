<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
      <title>Spring Boot MVC Security using Thymeleaf</title>
      <link rel="stylesheet" href="/css/styles.css"/>
</head>
<body>
   <h3>User Info Details</h3>
   <div>
	    Logged in user: <b th:inline="text"  class="user"> [[${#httpServletRequest.remoteUser}]] </b>
	    <form th:action="@{/app-logout}" method="POST">
	         <input type="submit" value="Logout"/>
	    </form>
   </div> <br/>	 
    <table>
	      <tr th:each="user : ${userinfo}">
		    <td th:text="${user.id}">Id</td>
			<td th:text="${user.uname}">Username</td>
			<td th:text="${user.password}">Password</td>
			<td th:text="${user.role}">Role</td>
	      </tr>
	</table>
</body>
</html>   