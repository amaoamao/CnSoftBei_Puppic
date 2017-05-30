# 狗蛋儿组API规范
```js
{
  "error": {
    "code": /* err_code here */,
    "message": /* err_description here */
  },
  "uploadedImageInfo": {
    // uploadedImageInfo here
  }
}
```

---

>   2017/4/8 - 2017/4/14

## 注册相关

### 是否已经注册		修改：增加头像字段
e.g.
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
  "isSignedUp": {
    "phone":"17761302891",
    "isSignedUp":false
    //如果未注册，则返回false
  }
}

{
  "error": {
    "code": 0,
    "message": ""
  },
  "isSignedUp": {
    "phone":"17761302891",
    "isSignedUp":true,
    "avatar":"cnsdnvcajspokjwopcvjiosajcs=="
    //如果已经注册，则返回true和头像
  }
}
```
### 发送手机验证码
```
GET http://xxx.xxx.xxx.xxx/signup/auth?phone=17761302891
```
返回示例：
```js
{
  "error": {
    "code": 0,
    "message": "发送成功"
    //如果发送失败，就返回别的error code和message
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
  "token": {
    "phone":"17761302891",
    "token":"sdjcfhsXZisdjfc2rcjs"
    //随机生成一个字符串用来在注册的时候验证
  }
}
```
### 注册	修改：增加头像字段

e.g. 
```shell
POST http://xxx.xxx.xxx.xxx/signup
{
	"user": {
      "name": "amaoamao",
      "phone": "17761302891",
      "psw": "123456",
      "gender": "male"
	},
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
### 登录	修改：不返回密码
e.g
```shell
POST http://xxx.xxx.xxx.xxx/login
{
  "phone": "17761302891",
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
  "user": {
    "name": "amaoamao",
    "phone": "17761302891",
    "gender": "male",
    "credit": 0,
    "is_admin": 0
  }
}
```





---

>   2017/4/15 - 2017/4/21

## 个人信息相关

### 获取个人信息

####  方式一

e.g.

```
GET http://xxx.xxx.xxx.xxx/17761302891
```

返回示例：

```js
{
  "error": {
    "code": 0,
    "message": ""
  },
  "user": {
    "name": "amaoamao",
    "phone": "17761302891",
    "gender": "male",
    "credit": 0,
    "is_admin": 0//因为头像字段太大，在这里不返回，只能使用下面的方式单独获取头像字段
  }
}
```

#### 方式二

```
GET http://xxx.xxx.xxx.xxx/17761302891/name
```

返回示例：

```javascript
{
  "error": {
    "code": 0,
    "message": ""
  },
  "str": "amaoamao"//如果要获取的字段是字符串，返回这个
  "integer":0//如果要获取的字段是数字，返回这个
}
```

其他字段的单独获取以此类推，包括name, **avatar**, gender, credit, is_admin

### 修改个人信息

e.g.

```shell
POST http://xxx.xxx.xxx.xxx/17761302891
{
  "name":"amao",
  "gender":"female"//更改了多少字段就传多少字段，遍历这个list就可以，目前包括name，avatar,gender,因为手机号修改比较复杂，暂不考虑
}
```

返回示例：

```javascript
{
  "error": {
    "code": 0,
    "message": "修改成功"
  }
}
```



## 我的钱包相关

### 积分商城

待添加

### 获取积分

待添加

### 积分流水

e.g.

```
GET http://xxx.xxx.xxx.xxx/17761302891/credit/history?offset=0&limit=20
```

返回示例：

```javascript
{
  "error": {
    "code": 0,
    "message": "查询成功"
  },
  "credit_histroy": [
    {
      "id": 123456,//此次识别的id，可以拿来查询这次识别的具体信息
      "time": 666666666666,//长整形，是积分变化的时间
      "description": "正确识别图片",
      "pic_id": 1//识别的图片的id
      "change": 1//积分变化
    }, {
      "id": 123457,
      "time": 666666666667,
      "description": "识别图片出错",
      "pic_id": 2
      "change": -1
    },......//具体返回条目数根据limit参数和offset参数决定
  ]
}
```





## 历史识别相关

### 获取我的识别历史

注：权限验证以后再说

#### 方式一

e.g.

```
GET http://xxx.xxx.xxx.xxx/17761302891/history?offset=0&limit=20
```

返回示例：

```javascript
{
  "error": {
    "code": 0,
    "message": ""
  },
  "historys": [
    {
      "id": 123456,//此次识别的id，和积分流水的id是一个，可以考虑积分流水表外键引用这个
      "time": 6666666666666,//长整形，为用户最后一次识别该图片的时间
      "pic_id": 1,//用户识别的图片ID
      "tags":[
        {
          "content": "狗",
          "isOrigin": true
        },{
          "content": "金毛狗",
          "isOrigin": true
        },{
          "content": "握手",
          "isOrigin": false
        }
      ]//用户给该图片打的标签
    }, {
      "id": 123457,
      "time": 6666666666667,
      "pic_id": 2,
      "tags":[
        {
          "content": "猫",
          "isOrigin": true
        },{
          "content": "金毛猫",
          "isOrigin": true
        }
      ]
    }, ......////具体返回条目数根据limit参数和offset参数决定
  ]
}
```

#### 方式二

e.g.

```
GET http://xxx.xxx.xxx.xxx/17761302891/history/123456
```

返回示例：

```javascript
{
  "error": {
    "code": 0,
    "message": ""
  },
  "history": {
      "id": 123456,//此次识别的id，和积分流水的id是一个，可以考虑积分流水表外键引用这个
      "time": 6666666666666,//长整形，为用户最后一次识别该图片的时间
      "pic_id": 1,//用户识别的图片ID
      "tags":[
        {
          "content": "狗",
          "isOrigin": true
        },{
          "content": "金毛狗",
          "isOrigin": true
        },{
          "content": "握手",
          "isOrigin": false
        }
      ]//用户给该图片打的标签
  }
}
```







## 获取图片相关

### 获取图片信息
## 删除size字段
e.g.

```
GET http://xxx.xxx.xxx.xxx/pic/123456/info
```

返回示例：

```javascript
{
  "error": {
    "code": 0,
    "message": ""
  },
  "info": {
    "category": [
      "动物","自然"
    ],
    "tag": [
      "猫","金毛猫"
    ]
  }
}
```



### 获取图片

e.g.

```
GET http://xxx.xxx.xxx.xxx/pic/123456
```

返回示例：

（后端内部做重定向，直接返回图片静态资源）



## 获取图片所有大类别
```
GET http://xxx.xxx.xxx.xxx/getAllCategories
```

返回示例：

```javascript
{
　　"error": 
　　{
　　"code": 0,
"message": "查询成功"
},
　　"cat_list":[‘airport’,’market’]
}
```
## 获取某一类别下所有图片索引（id）

```
GET http://xxx.xxx.xxx.xxx/pic_in_category?category=airport
```

返回示例：

```javascript
{
　　"error": 
　　{
　　"code": 0,
"message": "查询成功"
},
　　"img_list":[1,2,3]
}
```

## 获取用户偏好（大分类名称）

```
GET http://xxx.xxx.xxx.xxx/get_preference?phone=17713566666
```

返回示例：

```javascript
{
　　"error": 
　　{
　　"code": 0,
"message": "查询成功"
},
　　"pref_list":[‘airtport’,’market’]
}
```
## 添加一个用户偏好

```
POST http://xxx.xxx.xxx.xxx/get_preference
phone:177135xxxxx     //传入POST字段不是json
category:’airport’
```

返回示例：

```javascript
{
　　"error": 
　　{
　　"code": 0,
"message": "查询成功"
},
}
```

## 删除一个用户偏好

```
POST http://xxx.xxx.xxx.xxx/delete_preference
phone:177135xxxxx
category:’airport’
```

返回示例：

```javascript
{
　　"error": 
　　{
　　"code": 0,
"message": "删除用户偏好成功"
},
}
```

## 保存用户对一张图片的识别记录

```
POST http://xxx.xxx.xxx.xxx/177135xxxxx/save_history
Json
{
	“time”:666,
	“pic_id”:12,
	“tags”: [  {“tag”:’airport’,”isOrigin”:true},
				 {“tag”:’atm’,”isOrigin”:false}]
}
```

返回示例：

```javascript
{
　　"error": 
　　{
　　"code": 0,
"message": "历史插入成功"
},
}
```
## 修改稿用户对一张图片的识别记录

```
POST http://xxx.xxx.xxx.xxx/177135xxxxx/modify_history
Json
{
	“time”:666,
	“pic_id”:12,
	“tags”: [  				//传入修改过后的标签列表
{“tag”:’airport’,”isOrigin”:true}, 
				{“tag”:’atm’,”isOrigin”:false}
]
}
```

返回示例：

```javascript
{
　　"error": 
　　{
　　"code": 0,
"message": "历史修改成功"
},
}
```
