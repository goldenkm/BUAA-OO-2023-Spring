# Generated by Django 4.2.5 on 2023-11-19 09:00

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0002_post_category_alter_post_tags'),
    ]

    operations = [
        migrations.CreateModel(
            name='SideBar',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=50, verbose_name='模块名称')),
                ('display_type', models.PositiveIntegerField(choices=[(1, '搜索'), (2, '最新文章'), (3, '最热文章'), (4, '文章归档'), (5, 'HTML')], default=1, verbose_name='展示类型')),
                ('content', models.CharField(blank=True, default='', help_text='如果设置的不是HTML类型，可为空', max_length=500, verbose_name='内容')),
                ('sort', models.PositiveIntegerField(default=1, help_text='序号越大越靠前', verbose_name='排序')),
                ('status', models.PositiveIntegerField(choices=[(1, '隐藏'), (2, '展示')], default=2, verbose_name='状态')),
                ('add_date', models.DateTimeField(auto_now_add=True, verbose_name='创建时间')),
            ],
            options={
                'verbose_name': '侧边栏',
                'verbose_name_plural': '侧边栏',
                'ordering': ['-sort'],
            },
        ),
    ]
