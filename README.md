## 青果教务系统系列爬虫

#### 此爬虫针对的是我们学校(自己猜...)，但对与青果系统的教务系统应该都适用。略加修改即可
* [src/Get_JW](https://github.com/myvary/PDSU_JW/blob/master/src/GetJW/jw.java "教务管理系统登录")此文件为教务系统的模拟登录。
	* 从教务系统登录，验证码下载到本地后自动弹出。填写即可登录，登录的加密方式详见代码。
* [src/SZHGoJW_laoshi](https://github.com/myvary/PDSU_JW/blob/master/src/SZHGoJW_laoshi/laoshi.java "爬取教师信息")
	* 从校数字平台登录，由于一些不可描述的原因。反正能爬到几乎全校的教师信息。
* [src/pakebiao](https://github.com/myvary/PDSU_JW/blob/master/src/pakebiao/kebiao.java "爬去课表信息")
	* 从校数字平台登录，由于一些不可描述的原因。反正能爬到几乎全校学生的课表信息，并存入远程的mySQL数据库。
* [src/GoJW](https://github.com/myvary/PDSU_JW/blob/master/src/GoJW/SZHGoJW.java "学生信息")
	* 这个获取的信息有点敏感，大家谨慎食用。出问题了，不要来找我。**本人在此慎重声明！**
##### 近期有时间了，我会在我博客上写出来详细过程。大家可以关注一下：[刘鑫的博客](https://www.myvary.cn "刘鑫的博客")