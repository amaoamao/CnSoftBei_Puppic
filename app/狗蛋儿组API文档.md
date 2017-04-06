# 狗蛋儿组API规范
```js
{
  "error": {
    "code": /* err_code here */,
    "message": /* err_description here */
  },
  "data": {
    // data here
  }
}
```

## 注册相关
### 判断是否已经注册
eg.
```
GET http://xxx.xxx.xxx.xxx/signup?phone=17761302891
```
返回示例：
```js
{
  "error": {
    "code": 0,
    "message": ""
  },
  "data": {
    "phone":17761302891,
    "isSignedUp":false
    //如果未注册，则返回false，发送短信验证码到该手机号
    //如果已注册，则返回true
  }
}
```
### 验证手机验证码
e.g.
```
GET http://xxx.xxx.xxx.xxx/signup/auth?phone=17761302891&code=123456
```
返回示例：
```js
{
  "error": {
    "code": 0,
    "message": "验证成功"
    //如果验证码不对，就返回别的error code和message
  },
  "data": {
    "phone":17761302891,
    "token":"sdjcfhsXZisdjfc2rcjs"
    //随机生成一个字符串用来在注册的时候验证
  }
}
```
### 注册
e.g. 
```
POST http://xxx.xxx.xxx.xxx/signup
{
  "name": "amaoamao",
  "phone": 17761302891,
  "psw": "123456",
  "gender": "male",
  "token": "sdjcfhsXZisdjfc2rcjs"
}
```
返回示例：
```js
{
  "error": {
    "code": 0,
    "message": "注册成功"
  }
}
```
## 登录相关
### 登录
e.g
```
POST http://xxx.xxx.xxx.xxx/login
{
  "phone": 17761302891,
  "psw": "123456"
}
```
返回示例：
```js
{
  "error": {
    "code": 0,
    "message": "登录成功"
  },
  "data": {
    "name": "amaoamao",
    "phone": 17761302891,
    "psw": "123456",
    "gender": "male",
    "credit": 0,
    "is_admin": false
  }
}
```