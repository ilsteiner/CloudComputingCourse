<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Transcoder</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>    
    <div>
    	<p th:text="'Filepath: ' + $(fileUpload.getFile.getName())" />
    </div>
</body>
</html>