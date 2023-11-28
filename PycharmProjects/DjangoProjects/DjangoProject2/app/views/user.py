from django.shortcuts import render, redirect
from app import models

from app.utils.pagination import Pagination
from app.utils.model_form import UserModelForm


def user_list(request):
    """用户列表"""
    data_dict = {}
    search_data = request.GET.get('q', '')
    if search_data:
        data_dict["name__contains"] = search_data

    query_set = models.User.objects.filter(**data_dict)
    page = Pagination(request, queryset=query_set, page_size=10)
    context = {
        'search_data': search_data,
        'query_set': page.page_queryset,  # 分完页的数据
        'page_string': page.html()  # 页码
    }

    return render(request, 'user_list.html', context)


def user_register(request):
    """ 注册用户 """
    title = '注册用户'
    if request.method == 'GET':
        form = UserModelForm()
        return render(request, 'user_register.html', {'title': title, 'form': form})

    # 用户Post提交数据，并进行校验
    form = UserModelForm(data=request.POST)
    if form.is_valid():
        # 数据合法，保存到数据库
        form.save()
        user = models.User.objects.filter(**form.cleaned_data).first()
        # 网站生成随机字符串；写到用户浏览器的cookie中；再写入到session中；
        request.session['info'] = {'id': user.id, 'name': user.user_name}
        # session保存一天
        request.session.set_expiry(60 * 60 * 24)
        return redirect('/university/list/')
    else:
        # 校验失败，显示错误信息
        print(form.errors)
        return render(request, 'change.html', {'title': title, 'form': form})


def user_profile(request):
    nid = request.session['info'].get('id')
    user = models.User.objects.filter(id=nid).first()
    return render(request, 'user_profile.html', {'user': user})


def user_edit(request):
    """编辑用户"""
    nid = request.session['info'].get('id')
    user = models.User.objects.filter(id=nid).first()
    if request.method == 'GET':
        # 根据id去数据库获取要编辑的那一行数据
        form = UserModelForm(instance=user)
        return render(request, 'user_edit.html', {'form': form})

    form = UserModelForm(request.POST, request.FILES, instance=user)
    if form.is_valid():
        form.save()
        return redirect('/user/profile/')
    else:
        return render(request, 'user_edit.html', {'form': form})
