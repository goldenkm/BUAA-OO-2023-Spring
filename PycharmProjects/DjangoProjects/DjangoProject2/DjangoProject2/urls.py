"""
URL configuration for DjangoProject2 project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include
from app.views import university, account, user, major, post
from django.conf import settings
from django.conf.urls.static import static
from app.utils.upload import upload_file

urlpatterns = [
    # 管理员后台
    path('admin/', admin.site.urls),
    # 登陆
    path('login/', account.login),
    path('logout/', account.logout),
    path('image/code/', account.image_code),

    # 大学
    path('university/list/', university.university_list),
    path('university/<int:uid>/major/list/', university.university_major_list),

    # 用户
    path('user/register/', user.user_register),
    path('user/profile/', user.user_profile),
    path('user/edit/', user.user_edit),

    # 专业
    path('major/list/', major.major_list),

    # 帖子
    path('post/list/', post.post_list),
    path('post/category/<int:cid>/list/', post.category_list),
    path('post/<int:pid>/detail/', post.post_detail),
    path('post/search/', post.post_search),
    path('post/create/', post.post_create),

    # 上传文件
    path('', include('ckeditor_uploader.urls')),
] + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)  # 配置静态文件url


# 配置用户上传文件url
urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)

admin.site.site_header = "一研为定管理后台"
admin.site.index_title = "一研为定管理后台"
admin.site.site_title = "一研为定管理员登陆了"
