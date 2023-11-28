from django.shortcuts import render, redirect
from app import models
from app.utils.pagination import Pagination


# Create your views here.
def university_list(request):
    """ 学校列表 """
    data_dict = {}
    search_data = request.GET.get('q', '')
    if search_data:
        data_dict['name__contains'] = search_data
    query_set = models.University.objects.filter(**data_dict)
    page = Pagination(request, queryset=query_set, page_size=10)
    context = {
        'search_data': search_data,
        'query_set': page.page_queryset,
        'page_string': page.html()
    }
    return render(request, 'university_list.html', context)


def university_major_list(request, uid):
    majors = models.Major.objects.filter(university_id=uid).all()
    university = models.University.objects.filter(university_id=uid).first().name
    context = {'majors': majors, 'university': university}
    return render(request, 'major_list.html', context)


