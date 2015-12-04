<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Transcoder</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
    <form action="#" th:action="@{/transcoder}" th:object="${video}" method="post">
    	<div><label for="fileUpload">Upload video:</label><input type="file" accept="video/*" id="fileUpload" th:field="*{file}" /></div>
        <div><label for="preset1">HTML5 (WebM)</label><input type="radio" name="Preset" id="preset1" th:field="*{uploadType}" value="HTML5" checked/>
        <label for="preset2">Animated GIF</label><input type="radio" name="Preset" id="preset2" th:field="*{uploadType}" value="GIF"" /></div>
        <div><input type="submit" value="Submit" /> <input type="reset" value="Reset" /></div>
    </form>
</body>
</html>