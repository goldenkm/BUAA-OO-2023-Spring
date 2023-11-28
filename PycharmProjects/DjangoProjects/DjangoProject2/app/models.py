from django.db import models
from django.utils.functional import cached_property  # 缓存装饰器
from django.template.loader import render_to_string  # 渲染模版
from ckeditor.fields import RichTextField
from ckeditor_uploader.fields import RichTextUploadingField

# Create your models here.
major_in_buaa = (
    (1, '材料科学与工程学院'),
    (2, '电子信息工程学院'),
    (3, '自动化科学与电气工程学院'),
    (4, '能源与动力工程学院'),
    (5, '航空科学与工程学院'),
    (6, '计算机学院'),
    (7, '机械工程及自动化学院'),
    (8, '经济管理学院'),
    (9, '数学科学学院'),
    (10, '生物与医学工程学院'),
    (11, '人文社会科学学院'),
    (12, '外国语学院'),
    (13, '交通科学与工程学院'),
    (14, '可靠性与系统工程学院'),
    (15, '宇航学院'),
    (16, '飞行学院'),
    (17, '仪器科学与光电工程学院'),
    (18, '北京学院'),
    (19, '物理学院'),
    (20, '法学院'),
    (21, '软件学院'),
    (23, '高等理工学院'),
    (24, '中法工程师学院/国际通用工程师学院'),
    (25, '国际学院'),
    (26, '新媒体艺术与设计学院'),
    (27, '化学学院'),
    (28, '马克思主义学院'),
    (29, '人文与社会科学高等研究院'),
    (30, '空间与环境学院'),
    (39, '网络空间安全学院'),
    (41, '继续教育学院'),
    (42, '人工智能研究院'),
    (44, '研究生院'),
    (48, '前沿科学技术创新研究院'),
    (49, '集成电路科学与工程学院'),
)


# 用户表
class User(models.Model):
    user_name = models.CharField(verbose_name='用户名', max_length=32)
    password = models.CharField(verbose_name='密码', max_length=64)
    major_choices = major_in_buaa
    major = models.SmallIntegerField(verbose_name='本科专业', choices=major_choices)
    introduction = models.CharField(verbose_name='简介', max_length=100, blank=True, default="")
    age = models.IntegerField(verbose_name='年龄', default=0)
    gender_choices = ((1, '男'), (0, '女'))
    gender = models.SmallIntegerField(verbose_name='性别', choices=gender_choices, default=1)
    image = models.ImageField(upload_to='media/images', default='/images/default.png', blank=True, null=True)


# 学校表
class University(models.Model):
    university_id = models.IntegerField(verbose_name='院校代码', primary_key=True)
    name = models.CharField(verbose_name='学校名字', max_length=10)
    location = models.CharField(verbose_name='所在地区', max_length=16)
    mail = models.CharField(verbose_name='学校邮箱', max_length=32)
    phone = models.IntegerField(verbose_name='电话')

    class Meta:
        verbose_name = '大学'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.name


# 学院表（别的学院）
class School(models.Model):
    name = models.CharField(verbose_name='学院名称', max_length=16)
    university_id = models.ForeignKey(to='University', to_field='university_id', on_delete=models.CASCADE)


# 专业（别的大学）
class Major(models.Model):
    # 正式版需要把这里换成CharField
    major_id = models.SmallIntegerField(verbose_name='专业代码', primary_key=True)
    major_name = models.CharField(verbose_name='专业名称', max_length=16)
    university_id = models.ForeignKey(to='University', to_field='university_id', verbose_name='所属大学',
                                      on_delete=models.CASCADE)

    class Meta:
        verbose_name = '专业'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.major_name


# 考试科目
class ExamSubject(models.Model):
    major_id = models.ForeignKey(to='Major', to_field='major_id', on_delete=models.CASCADE)
    university_id = models.ForeignKey(to='University', to_field='university_id', on_delete=models.CASCADE)
    professional_exam1 = models.CharField(verbose_name='专业课1', max_length=16)
    professional_exam2 = models.CharField(verbose_name='专业课2', max_length=16)
    english_exam = models.CharField(verbose_name='英语', max_length=16)
    politic_exam = models.CharField(verbose_name='政治', max_length=16)


# 录取信息表
class AdmitInformation(models.Model):
    total_score = models.IntegerField(verbose_name='总分')
    score1 = models.IntegerField(verbose_name='初试分数')
    score2 = models.IntegerField(verbose_name='复试分数')
    rank = models.SmallIntegerField(verbose_name='排名')
    year = models.IntegerField(verbose_name='年份')
    major_id = models.ForeignKey(to='Major', to_field='major_id', on_delete=models.CASCADE)


# 帖子表
class Post(models.Model):
    title = models.CharField(verbose_name='帖子标题', max_length=16)
    content = RichTextUploadingField(verbose_name='帖子正文', max_length=5000)
    create_time = models.DateTimeField(verbose_name='帖子创建时间')
    author = models.ForeignKey(to='User', to_field='id', verbose_name='帖子作者', on_delete=models.CASCADE)
    tags = models.ForeignKey(to='Tag', on_delete=models.CASCADE, blank=True, null=True, verbose_name='标签')
    category = models.ForeignKey(to='Category', on_delete=models.CASCADE, verbose_name='分类')

    # 正式版本需要加上
    # create_date = models.DateTimeField(auto_now_add=True, verbose_name="创建时间")
    # edit_date = models.DateTimeField(auto_now=True, verbose_name="修改时间")

    class Meta:
        verbose_name = '帖子'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.title


# 通知表
class Notice(models.Model):
    post_id = models.ForeignKey(to='Post', to_field='id', on_delete=models.CASCADE)
    comment_id = models.ForeignKey(to='Comment', to_field='id', on_delete=models.CASCADE)
    commenter_id = models.ForeignKey(to='User', to_field='id', on_delete=models.CASCADE, related_name='commenter_id')
    owner_id = models.ForeignKey(to='User', to_field='id', on_delete=models.CASCADE, related_name='owner_id')
    time = models.DateTimeField(verbose_name='通知创建时间')


# 评论表
class Comment(models.Model):
    post_id = models.ForeignKey(to='Post', to_field='id', on_delete=models.CASCADE)
    # father_id = models.IntegerField(verbose_name='父级评论id')
    # like_count = models.IntegerField(verbose_name='点赞数')
    author = models.ForeignKey(to='User', to_field='id', verbose_name='帖子作者', on_delete=models.CASCADE)
    create_time = models.DateTimeField(verbose_name='评论创建时间', auto_now=True)
    content = RichTextField(verbose_name='评论内容', max_length=200)


# 点赞关系表
class Like(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    post = models.ForeignKey(Post, on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now_add=True)


# 帖子分类
class Category(models.Model):
    name = models.CharField(max_length=32, verbose_name='种类名称')
    description = models.TextField(max_length=200, blank=True, default="", verbose_name="描述")
    create_date = models.DateTimeField(auto_now_add=True, verbose_name="创建时间")
    edit_date = models.DateTimeField(auto_now=True, verbose_name="修改时间")

    class Meta:
        verbose_name = "帖子分类"

    def __str__(self):
        return self.name


class Tag(models.Model):
    name = models.CharField(max_length=10, verbose_name='文章标签')
    create_date = models.DateTimeField(auto_now_add=True, verbose_name="创建时间")
    edit_date = models.DateTimeField(auto_now=True, verbose_name="修改时间")

    class Meta:
        verbose_name = '帖子标签'

    def __str__(self):
        return self.name


class SideBar(models.Model):
    # 侧边栏的模型数据

    STATUS = (
        (1, '隐藏'),
        (2, '展示')
    )

    DISPLAY_TYPE = (
        (1, '搜索'),
        (2, '最新文章'),
        (3, '最热文章'),
        (4, '文章归档'),
        (5, 'HTML')
    )

    title = models.CharField(max_length=50, verbose_name="模块名称")  # 模块名称
    display_type = models.PositiveIntegerField(default=1, choices=DISPLAY_TYPE,
                                               verbose_name="展示类型")  # 侧边栏  搜索框/最新文章/热门文章/HTML自定义等
    content = models.CharField(max_length=500, blank=True, default='', verbose_name="内容",
                               help_text="如果设置的不是HTML类型，可为空")  # 这个字段是专门用来给HTML类型用的，其他类型可为空
    sort = models.PositiveIntegerField(default=1, verbose_name="排序", help_text='序号越大越靠前')
    status = models.PositiveIntegerField(default=2, choices=STATUS, verbose_name="状态")  # 隐藏  显示状态
    add_date = models.DateTimeField(auto_now_add=True, verbose_name="创建时间")  # 时间

    class Meta:
        verbose_name = "侧边栏"
        verbose_name_plural = verbose_name
        ordering = ['-sort']

    def __str__(self):
        return self.title

    @classmethod  # 类方法装饰器，这个就变成了这个类的一个方法可以调用
    def get_sidebar(cls):
        return cls.objects.filter(status=2)  # 查询到所有允许展示的模块

    @property  # 成为一个类属性，调用的时候不需要后边的（）,是只读的，用户没办法修改
    def get_content(self):
        if self.display_type == 1:
            context = {

            }
            return render_to_string('sidebar/post_search.html', context=context)
        elif self.display_type == 2:
            context = {

            }
            return render_to_string('sidebar/post_new.html', context=context)
        elif self.display_type == 3:
            context = {

            }
            return render_to_string('sidebar/post_hot.html', context=context)
        elif self.display_type == 4:  # 文章归档
            context = {

            }
            return render_to_string('sidebar/post_archive.html', context=context)
        elif self.display_type == 5:  # 自定义侧边栏

            return self.content  # 在侧边栏直接使用这里的html，模板中必须使用safe过滤器去渲染HTML
