from django.shortcuts import render, redirect
from app import models

from app.utils.pagination import Pagination
from app.utils.model_form import MajorModelForm
from django.contrib.auth.decorators import login_required


def major_list(request):
    majors = models.Major.objects.filter().all()
    return render(request, 'major_list.html', {'majors': majors})
