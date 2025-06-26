// 安全监控管理JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // 全局变量
    let currentPage = 0;
    let pageSize = 10;
    let totalPages = 0;
    let totalRecords = 0;
    let currentRecordId = null;
    
    // 初始化
    loadSafetyRecords();
    
    // 事件监听
    document.getElementById('searchForm').addEventListener('submit', function(e) {
        e.preventDefault();
        currentPage = 0;
        loadSafetyRecords();
    });
    
    document.getElementById('resetBtn').addEventListener('click', function() {
        document.getElementById('searchForm').reset();
        currentPage = 0;
        loadSafetyRecords();
    });
    
    document.getElementById('saveRecordBtn').addEventListener('click', saveSafetyRecord);
    document.getElementById('editFromViewBtn').addEventListener('click', editFromView);
    
    // 加载安全记录
    function loadSafetyRecords() {
        const searchParams = new URLSearchParams();
        
        // 获取搜索参数
        const workerId = document.getElementById('workerId').value;
        const projectId = document.getElementById('projectId').value;
        const eventType = document.getElementById('eventType').value;
        const severityLevel = document.getElementById('severityLevel').value;
        const status = document.getElementById('status').value;
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;
        const location = document.getElementById('location').value;
        
        // 添加搜索参数
        if (workerId) searchParams.append('workerId', workerId);
        if (projectId) searchParams.append('projectId', projectId);
        if (eventType) searchParams.append('eventType', eventType);
        if (severityLevel) searchParams.append('severityLevel', severityLevel);
        if (status) searchParams.append('status', status);
        if (startDate) searchParams.append('startDate', startDate);
        if (endDate) searchParams.append('endDate', endDate);
        if (location) searchParams.append('location', location);
        
        // 添加分页参数
        searchParams.append('page', currentPage);
        searchParams.append('size', pageSize);
        
        // 发送请求
        axios.get(`/api/safety/records?${searchParams.toString()}`)
            .then(function(response) {
                const data = response.data;
                renderSafetyRecords(data.content);
                renderPagination(data.totalPages, data.totalElements);
            })
            .catch(function(error) {
                console.error('加载安全记录失败:', error);
                alert('加载安全记录失败，请稍后重试');
            });
    }
    
    // 渲染安全记录表格
    function renderSafetyRecords(records) {
        const tableBody = document.getElementById('safetyRecordTableBody');
        tableBody.innerHTML = '';
        
        if (records.length === 0) {
            const row = document.createElement('tr');
            row.innerHTML = `<td colspan="10" class="text-center">暂无数据</td>`;
            tableBody.appendChild(row);
            return;
        }
        
        records.forEach(record => {
            const row = document.createElement('tr');
            
            // 设置严重程度的样式类
            let severityClass = '';
            if (record.severityLevel === '特别严重') {
                severityClass = 'severity-high';
            } else if (record.severityLevel === '严重') {
                severityClass = 'severity-medium';
            } else {
                severityClass = 'severity-low';
            }
            
            // 设置状态的样式类
            let statusClass = '';
            if (record.status === '待处理') {
                statusClass = 'status-pending';
            } else if (record.status === '处理中') {
                statusClass = 'status-processing';
            } else if (record.status === '已处理') {
                statusClass = 'status-resolved';
            } else if (record.status === '已关闭') {
                statusClass = 'status-closed';
            }
            
            // 格式化日期时间
            const occurrenceTime = formatDateTime(record.occurrenceTime);
            
            row.innerHTML = `
                <td>${record.id}</td>
                <td>${record.workerId}</td>
                <td>${record.projectId}</td>
                <td>${record.eventType}</td>
                <td class="${severityClass}">${record.severityLevel}</td>
                <td>${occurrenceTime}</td>
                <td>${record.location}</td>
                <td class="${statusClass}">${record.status}</td>
                <td>${record.handler || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-info view-record" data-id="${record.id}">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-primary edit-record" data-id="${record.id}">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger delete-record" data-id="${record.id}">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            `;
            
            tableBody.appendChild(row);
        });
        
        // 添加事件监听
        document.querySelectorAll('.view-record').forEach(button => {
            button.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                viewSafetyRecord(id);
            });
        });
        
        document.querySelectorAll('.edit-record').forEach(button => {
            button.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                editSafetyRecord(id);
            });
        });
        
        document.querySelectorAll('.delete-record').forEach(button => {
            button.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                deleteSafetyRecord(id);
            });
        });
    }
    
    // 渲染分页
    function renderPagination(totalPages, totalElements) {
        const pagination = document.getElementById('pagination');
        pagination.innerHTML = '';
        
        document.getElementById('totalRecords').textContent = totalElements;
        
        if (totalPages <= 1) {
            return;
        }
        
        // 上一页
        const prevLi = document.createElement('li');
        prevLi.className = `page-item ${currentPage === 0 ? 'disabled' : ''}`;
        prevLi.innerHTML = `
            <a class="page-link" href="#" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        `;
        prevLi.addEventListener('click', function(e) {
            e.preventDefault();
            if (currentPage > 0) {
                currentPage--;
                loadSafetyRecords();
            }
        });
        pagination.appendChild(prevLi);
        
        // 页码
        for (let i = 0; i < totalPages; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${currentPage === i ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#">${i + 1}</a>`;
            li.addEventListener('click', function(e) {
                e.preventDefault();
                currentPage = i;
                loadSafetyRecords();
            });
            pagination.appendChild(li);
        }
        
        // 下一页
        const nextLi = document.createElement('li');
        nextLi.className = `page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}`;
        nextLi.innerHTML = `
            <a class="page-link" href="#" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        `;
        nextLi.addEventListener('click', function(e) {
            e.preventDefault();
            if (currentPage < totalPages - 1) {
                currentPage++;
                loadSafetyRecords();
            }
        });
        pagination.appendChild(nextLi);
    }
    
    // 查看安全记录
    function viewSafetyRecord(id) {
        axios.get(`/api/safety/records/${id}`)
            .then(function(response) {
                const record = response.data;
                
                // 填充详情模态框
                document.getElementById('viewId').textContent = record.id;
                document.getElementById('viewWorkerId').textContent = record.workerId;
                document.getElementById('viewProjectId').textContent = record.projectId;
                document.getElementById('viewEventType').textContent = record.eventType;
                document.getElementById('viewSeverityLevel').textContent = record.severityLevel;
                document.getElementById('viewOccurrenceTime').textContent = formatDateTime(record.occurrenceTime);
                document.getElementById('viewLocation').textContent = record.location;
                document.getElementById('viewStatus').textContent = record.status;
                document.getElementById('viewHandler').textContent = record.handler || '-';
                document.getElementById('viewHandleTime').textContent = record.handleTime ? formatDateTime(record.handleTime) : '-';
                document.getElementById('viewDescription').textContent = record.description;
                document.getElementById('viewMeasures').textContent = record.measures || '-';
                document.getElementById('viewResult').textContent = record.result || '-';
                document.getElementById('viewRemarks').textContent = record.remarks || '-';
                
                // 显示详情模态框
                const viewModal = new bootstrap.Modal(document.getElementById('viewRecordModal'));
                viewModal.show();
                
                // 保存当前记录ID
                currentRecordId = id;
            })
            .catch(function(error) {
                console.error('获取安全记录详情失败:', error);
                alert('获取安全记录详情失败，请稍后重试');
            });
    }
    
    // 编辑安全记录
    function editSafetyRecord(id) {
        axios.get(`/api/safety/records/${id}`)
            .then(function(response) {
                const record = response.data;
                
                // 填充表单
                document.getElementById('recordId').value = record.id;
                document.getElementById('modalWorkerId').value = record.workerId;
                document.getElementById('modalProjectId').value = record.projectId;
                document.getElementById('modalEventType').value = record.eventType;
                document.getElementById('modalSeverityLevel').value = record.severityLevel;
                document.getElementById('modalOccurrenceTime').value = formatDateTimeForInput(record.occurrenceTime);
                document.getElementById('modalLocation').value = record.location;
                document.getElementById('modalDescription').value = record.description;
                document.getElementById('modalStatus').value = record.status;
                document.getElementById('modalHandler').value = record.handler || '';
                document.getElementById('modalHandleTime').value = record.handleTime ? formatDateTimeForInput(record.handleTime) : '';
                document.getElementById('modalMeasures').value = record.measures || '';
                document.getElementById('modalResult').value = record.result || '';
                document.getElementById('modalRemarks').value = record.remarks || '';
                
                // 显示模态框
                const modal = new bootstrap.Modal(document.getElementById('safetyRecordModal'));
                modal.show();
                
                // 保存当前记录ID
                currentRecordId = id;
            })
            .catch(function(error) {
                console.error('获取安全记录详情失败:', error);
                alert('获取安全记录详情失败，请稍后重试');
            });
    }
    
    // 从查看详情模态框切换到编辑模态框
    function editFromView() {
        // 关闭查看详情模态框
        const viewModal = bootstrap.Modal.getInstance(document.getElementById('viewRecordModal'));
        viewModal.hide();
        
        // 打开编辑模态框
        editSafetyRecord(currentRecordId);
    }
    
    // 保存安全记录
    function saveSafetyRecord() {
        // 获取表单数据
        const recordId = document.getElementById('recordId').value;
        const workerId = document.getElementById('modalWorkerId').value;
        const projectId = document.getElementById('modalProjectId').value;
        const eventType = document.getElementById('modalEventType').value;
        const severityLevel = document.getElementById('modalSeverityLevel').value;
        const occurrenceTime = document.getElementById('modalOccurrenceTime').value;
        const location = document.getElementById('modalLocation').value;
        const description = document.getElementById('modalDescription').value;
        const status = document.getElementById('modalStatus').value;
        const handler = document.getElementById('modalHandler').value;
        const handleTime = document.getElementById('modalHandleTime').value;
        const measures = document.getElementById('modalMeasures').value;
        const result = document.getElementById('modalResult').value;
        const remarks = document.getElementById('modalRemarks').value;
        
        // 构建请求数据
        const data = {
            workerId: parseInt(workerId),
            projectId: parseInt(projectId),
            eventType: eventType,
            severityLevel: severityLevel,
            occurrenceTime: occurrenceTime,
            location: location,
            description: description,
            status: status,
            handler: handler || null,
            handleTime: handleTime || null,
            measures: measures || null,
            result: result || null,
            remarks: remarks || null
        };
        
        // 如果有ID，则更新；否则创建
        const url = recordId ? `/api/safety/records/${recordId}` : '/api/safety/records';
        const method = recordId ? 'put' : 'post';
        
        // 发送请求
        axios[method](url, data)
            .then(function(response) {
                // 关闭模态框
                const modal = bootstrap.Modal.getInstance(document.getElementById('safetyRecordModal'));
                modal.hide();
                
                // 重新加载数据
                loadSafetyRecords();
                
                // 显示成功消息
                alert(recordId ? '更新成功' : '创建成功');
            })
            .catch(function(error) {
                console.error('保存安全记录失败:', error);
                alert('保存安全记录失败，请稍后重试');
            });
    }
    
    // 删除安全记录
    function deleteSafetyRecord(id) {
        if (confirm('确定要删除这条安全记录吗？')) {
            axios.delete(`/api/safety/records/${id}`)
                .then(function() {
                    // 重新加载数据
                    loadSafetyRecords();
                    
                    // 显示成功消息
                    alert('删除成功');
                })
                .catch(function(error) {
                    console.error('删除安全记录失败:', error);
                    alert('删除安全记录失败，请稍后重试');
                });
        }
    }
    
    // 格式化日期时间
    function formatDateTime(dateTimeStr) {
        if (!dateTimeStr) return '-';
        
        const date = new Date(dateTimeStr);
        return date.toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    }
    
    // 格式化日期时间为input datetime-local格式
    function formatDateTimeForInput(dateTimeStr) {
        if (!dateTimeStr) return '';
        
        const date = new Date(dateTimeStr);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        
        return `${year}-${month}-${day}T${hours}:${minutes}`;
    }
}); 