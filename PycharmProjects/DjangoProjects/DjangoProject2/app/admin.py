from django.contrib import admin

# Register your models here.
from .models import Category, Post, Tag, SideBar, University, Major

admin.site.register(Category)
admin.site.register(Post)
admin.site.register(Tag)
admin.site.register(SideBar)
admin.site.register(University)
admin.site.register(Major)
