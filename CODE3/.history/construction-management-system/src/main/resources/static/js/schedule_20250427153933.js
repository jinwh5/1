// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 初始化搜索表单提交事件
    initSearchForm();
});

// 初始化搜索表单
function initSearchForm() {
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(searchForm);
            const params = new URLSearchParams();
            
            for (let [key, value] of formData.entries()) {
                if (value) {
                    params.append(key, value);
                }
            }
            
            window.location.href = `/schedule?${params.toString()}`;
        });
    }
}

// 创建排班
function createSchedule() {
    const form = document.getElementById('createScheduleForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const schedule = {
        workerId: document.getElementById('createWorkerId').value,
        projectId: document.getElementById('createProjectId').value,
        date: document.getElementById('createDate').value,
        shiftType: document.getElementById('createShiftType').value,
        startTime: document.getElementById('createStartTime').value,
        endTime: document.getElementById('createEndTime').value,
        location: document.getElementById('createLocation').value
    };

    fetch('/api/schedules', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(schedule)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('创建排班失败');
        }
        return response.json();
    })
    .then(data => {
        alert('创建排班成功');
        window.location.reload();
    })
    .catch(error => {
        alert(error.message);
    });
}

// 编辑排班
function editSchedule(id) {
    // 获取排班详情
    fetch(`/api/schedules/${id}`)
        .then(response => response.json())
        .then(schedule => {
            // TODO: 实现编辑模态框
            alert('编辑功能开发中');
        })
        .catch(error => {
            alert('获取排班信息失败');
        });
}

// 删除排班
function deleteSchedule(id) {
    if (!confirm('确定要删除这个排班吗？')) {
        return;
    }

    fetch(`/api/schedules/${id}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('删除排班失败');
        }
        alert('删除排班成功');
        window.location.reload();
    })
    .catch(error => {
        alert(error.message);
    });
}

// 检查排班冲突
function checkConflict(id) {
    fetch(`/api/schedules/${id}/conflict`)
        .then(response => response.json())
        .then(data => {
            if (data.hasConflict) {
                alert(`发现冲突：${data.conflictMessage}`);
            } else {
                alert('未发现冲突');
            }
        })
        .catch(error => {
            alert('检查冲突失败');
        });
}

// 更新天气信息
function updateWeather(id) {
    fetch(`/api/schedules/${id}/weather`, {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        alert(`天气更新成功\n天气状况：${data.weatherCondition}\n是否适合施工：${data.suitableForWork ? '是' : '否'}\n建议：${data.workSuggestion}`);
        window.location.reload();
    })
    .catch(error => {
        alert('更新天气信息失败');
    });
} 