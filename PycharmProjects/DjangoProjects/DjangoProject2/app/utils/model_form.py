from app import models
from django import forms
from django.core.validators import RegexValidator
from django.core.exceptions import ValidationError
from app.utils.bootstrap import BootstrapModelForm
from app.utils.encrypt import md5
from ckeditor.widgets import CKEditorWidget


class LoginModelForm(BootstrapModelForm):
    img_code = forms.CharField(label='验证码')

    class Meta:
        model = models.User
        fields = ['user_name', 'password']
        widgets = {
            'user_name': forms.TextInput,
            'password': forms.PasswordInput,
            'img_code': forms.TextInput
        }


class UserModelForm(BootstrapModelForm):
    # 为了添加约束条件可以在这里重写字段（例如min_length只能在这约束）
    user_name = forms.CharField(min_length=2, label='用户名')
    # 校验数据
    #   -方法1
    password = forms.CharField(label='密码',
                               validators=[RegexValidator('^[a-zA-Z0-9]{6,}+$', '密码只能由至少6位的字母或数字组成')])

    class Meta:
        model = models.User
        fields = ['user_name', 'password', 'major', 'introduction', 'age', 'gender', 'introduction', 'image']
        widgets = {
            'user_name': forms.TextInput(attrs={'class': 'form-control'}),
            'password': forms.PasswordInput(attrs={'class': 'form-control'}),
            'introduction': forms.TextInput(attrs={'class': 'form-control'})
        }

    # 校验数据
    #   -方法2
    def clean_age(self):
        txt_age = self.cleaned_data['age']  # 用户传入的数据
        if txt_age < 18 or txt_age > 30:
            raise ValidationError('年龄范围错误')
        # 验证通过，把用户输入的数据返回
        return txt_age

    # 用户不允许重名的功能
    def clean_name(self):
        # 当前编辑的那一行的id（pk = primary key）
        # self.instance.pk
        input_name = self.cleaned_data['user_name']
        if models.UserInfo.objects.exclude(id=self.instance.pk).filter(name=input_name).exists():
            raise ValidationError('姓名重复')
        return input_name

    # # 校验两次密码是否一致，可加可不加
    # def clean_password1(self):
    #     if self.cleaned_data['password'] != self.cleaned_data['password1']:
    #         raise ValidationError('两次密码不一致')
    #     return self.cleaned_data['password1']


class MajorModelForm(BootstrapModelForm):
    class Meta:
        model = models.Major
        fields = ['major_id', 'major_name', 'university_id']


class PostModelForm(BootstrapModelForm):

    class Meta:
        model = models.Post
        fields = ['title', 'content', 'create_time', 'author', 'tags', 'category']
        widgets = {
            'content': CKEditorWidget()
        }


class CommentModelForm(BootstrapModelForm):
    class Meta:
        model = models.Comment
        fields = ['content']
        widgets = {
            'content': CKEditorWidget()
        }
