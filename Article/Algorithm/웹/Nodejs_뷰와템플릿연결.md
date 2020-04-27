# Node.js 뷰와 템플릿 연결


## 


## 전체 디렉토리 구조




``` javascript
const express = require('express');
const nameController = require("./controllers/nameController");

app = express();

app.set("port", process.env.PORT || 3000);
app.set("view engine", "ejs");

app.get("/name", nameController.respondeWithName);


app.listen(app.get("port"), () => {
    console.log(`Server running at http://localhost:${app.get("port")}`);
  });
```
<center>app.js</center>




``` javascript
exports.respondeWithName = (req, res) => {
    res.render("index");
}
```
<center>nameControllers.js</center>



``` javascript
<% let name = "Jon"; %>
<h1> hello <%= name %> </h1>
```
<center> index.ejs</center>





``` javascript


```







