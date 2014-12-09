<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>CategoryWindow</title>
</head>

<body>
<h1>DawgTrades</h1>
<h3>You are logged in as ${username}</h3>

<p>
Please select a category!
<div class="boxed">
  <form action="findSubcategoriesOfCategory" method="Get">
    <p>
      <div>
      <c:forEach items="${categories}" var="cat">
      	<p>
	  <option value="${cat[0]}">${cat[0]}</option>
	</p>
      </c:forEach>
      </div>
    </p>
  </form>
</div>
</p>

</body>
</html>