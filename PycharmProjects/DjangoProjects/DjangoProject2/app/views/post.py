from django.shortcuts import render, redirect, get_object_or_404
from app import models
from django.db.models import Q, F
from app.utils.pagination import Pagination
from app.utils.model_form import UserModelForm, PostModelForm, CommentModelForm


def post_list(request):
    categories = models.Category.objects.all()  # 查询到的所有分类
    posts = models.Post.objects.all()  # 查询到的所有帖子
    page = Pagination(request, queryset=posts)
    context = {'categories': categories, 'posts': posts, 'page_str': page.html()}
    return render(request, 'post_list.html', context=context)


def category_list(request, cid):
    category = get_object_or_404(models.Category, id=cid)
    # 获取当前分类下的所有文章
    posts = category.post_set.all()
    page = Pagination(request, queryset=posts)
    context = {'category': category, 'posts': page.page_queryset, 'page_str': page.html()}
    return render(request, 'post_category_list.html', context)


def post_detail(request, pid):
    """文章详情页"""
    post = get_object_or_404(models.Post, id=pid)
    # 查询点赞数量
    user_id = request.session['info'].get('id')
    user = models.User.objects.filter(id=user_id).first()
    has_liked = models.Like.objects.filter(user=user, post_id=pid)
    # 查询评论
    comments = models.Comment.objects.filter(post_id=pid)
    if request.method == 'POST':
        # 点赞需要另一种方式来实现，通过提交表单有点愚蠢
        # if not has_liked:
        #     like = models.Like(user=user, post_id=pid)
        #     like.save()
        # else:           # 如果点赞过，就删除点赞关系
        #     has_liked.delete()
        content = request.POST.get('content')
        print(user_id)
        comment = models.Comment(post_id=post, author=user, content=content)
        comment.save()
        return redirect('/post/' + str(post.id) + '/detail/')
    prev = models.Post.objects.filter(id__lt=pid).last()
    after = models.Post.objects.filter(id__gt=pid).first()
    like_count = models.Like.objects.filter(post_id=pid).count()
    new_comment = CommentModelForm
    context = {
        'post': post,
        'prev': prev,
        'after': after,
        'like_count': like_count,
        'comments': comments,
        'new_comment': new_comment
    }
    return render(request, 'post_detail.html', context=context)


def post_search(request):
    """ 搜索视图 """
    keyword = request.GET.get('keyword')
    if not keyword:
        posts = models.Post.objects.all()
    else:
        posts = models.Post.objects.filter(Q(title__icontains=keyword) | Q(content__icontains=keyword))
    context = {
        'posts': posts
    }
    return render(request, 'post_list.html', context=context)


def post_create(request):
    if request.method == 'POST':
        title = request.POST.get('title')
        content = request.POST.get('content')
        author = models.User.objects.filter(id=request.session['info'].get('id')).first()
        category = models.Category.objects.filter(id=1).first()
        form = PostModelForm(data={
            'title': title,
            'content': content,
            'author': author,
            'category': category
        }
        )
        print(form.is_valid())
        if form.is_valid():
            post = form.save()
            return redirect('/post/' + str(post.id) + '/detail/')
    form = PostModelForm()
    context = {'form': form}
    return render(request, 'post_create.html', context)
