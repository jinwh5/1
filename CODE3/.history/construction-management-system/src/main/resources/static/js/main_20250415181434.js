// 等待DOM加载完成
document.addEventListener('DOMContentLoaded', function() {
    // 更新当前时间
    updateCurrentTime();
    setInterval(updateCurrentTime, 1000);
    
    // 初始化统计数字动画
    initCounters();
    
    // 初始化特性卡片悬停效果
    initFeatureCards();
    
    // 初始化导航栏活跃状态
    setActiveNavItem();
    
    // 初始化侧边栏菜单激活状态
    initializeActiveMenu();
    
    // 初始化提示消息关闭功能
    initializeAlertClosers();
    
    // 初始化响应式菜单
    initializeResponsiveMenu();
    
    // 初始化工具提示
    initializeTooltips();
    
    // 初始化表单验证
    initializeFormValidation();
    
    // 初始化数据表格交互
    initializeDataTables();
});

/**
 * 更新当前时间显示
 */
function updateCurrentTime() {
    const timeElement = document.getElementById('current-time');
    if (!timeElement) return;
    
    const now = new Date();
    const options = { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric', 
        hour: '2-digit', 
        minute: '2-digit', 
        second: '2-digit',
        hour12: false
    };
    timeElement.textContent = now.toLocaleDateString('zh-CN', options);
}

/**
 * 初始化统计数字动画
 */
function initCounters() {
    const counters = document.querySelectorAll('.counter');
    const speed = 200; // 动画速度
    
    counters.forEach(counter => {
        const targetValue = parseInt(counter.getAttribute('data-target'));
        const startValue = 0;
        const duration = 1500; // 持续时间（毫秒）
        const increment = targetValue / (duration / speed);
        
        let currentValue = startValue;
        
        const updateCounter = () => {
            currentValue += increment;
            
            if (currentValue < targetValue) {
                counter.textContent = Math.ceil(currentValue);
                setTimeout(updateCounter, speed);
            } else {
                counter.textContent = targetValue;
            }
        };
        
        // 创建Intersection Observer以在元素可见时开始动画
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    updateCounter();
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.1 });
        
        observer.observe(counter);
    });
}

/**
 * 初始化特性卡片悬停效果
 */
function initFeatureCards() {
    const featureCards = document.querySelectorAll('.feature-card');
    
    featureCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            const icon = this.querySelector('.card-icon i');
            if (icon) {
                icon.classList.add('fa-bounce');
            }
        });
        
        card.addEventListener('mouseleave', function() {
            const icon = this.querySelector('.card-icon i');
            if (icon) {
                icon.classList.remove('fa-bounce');
            }
        });
    });
}

/**
 * 设置当前页面对应的导航项为活跃状态
 */
function setActiveNavItem() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPath || 
            (currentPath === '/' && href === '/index') || 
            (currentPath === '/index.html' && href === '/index')) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

/**
 * 工人管理相关功能
 */
const WorkerManager = {
    /**
     * 初始化工人管理页面功能
     */
    init: function() {
        // 绑定表单提交事件
        const workerForm = document.getElementById('worker-form');
        if (workerForm) {
            workerForm.addEventListener('submit', this.handleFormSubmit);
        }
        
        // 绑定删除按钮点击事件
        const deleteButtons = document.querySelectorAll('.btn-delete-worker');
        deleteButtons.forEach(button => {
            button.addEventListener('click', this.handleDelete);
        });
        
        // 绑定编辑按钮点击事件
        const editButtons = document.querySelectorAll('.btn-edit-worker');
        editButtons.forEach(button => {
            button.addEventListener('click', this.handleEdit);
        });
    },
    
    /**
     * 处理表单提交
     */
    handleFormSubmit: function(event) {
        // 表单验证逻辑
        const form = event.target;
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
            form.classList.add('was-validated');
            return;
        }
        
        // 显示提交反馈
        const submitButton = form.querySelector('button[type="submit"]');
        if (submitButton) {
            submitButton.disabled = true;
            submitButton.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>提交中...';
        }
    },
    
    /**
     * 处理删除工人
     */
    handleDelete: function(event) {
        const workerId = this.getAttribute('data-id');
        const workerName = this.getAttribute('data-name');
        
        if (confirm(`确定要删除工人 "${workerName}" 吗？这个操作不能撤销。`)) {
            // 继续删除操作
            console.log(`删除工人 ID: ${workerId}`);
        } else {
            event.preventDefault();
        }
    },
    
    /**
     * 处理编辑工人
     */
    handleEdit: function() {
        const workerId = this.getAttribute('data-id');
        console.log(`编辑工人 ID: ${workerId}`);
        
        // 可以通过AJAX加载工人信息，并填充到表单中
    }
};

/**
 * 考勤管理相关功能
 */
const AttendanceManager = {
    /**
     * 初始化考勤管理页面功能
     */
    init: function() {
        // 绑定日期筛选事件
        const dateFilter = document.getElementById('attendance-date-filter');
        if (dateFilter) {
            dateFilter.addEventListener('change', this.handleDateFilter);
        }
        
        // 初始化考勤录入功能
        this.initAttendanceEntry();
    },
    
    /**
     * 处理日期筛选
     */
    handleDateFilter: function() {
        const selectedDate = this.value;
        console.log(`筛选日期: ${selectedDate}`);
        
        // 可以通过AJAX加载选定日期的考勤记录
    },
    
    /**
     * 初始化考勤录入功能
     */
    initAttendanceEntry: function() {
        const entryForm = document.getElementById('attendance-entry-form');
        if (entryForm) {
            entryForm.addEventListener('submit', function(event) {
                event.preventDefault();
                
                // 收集表单数据
                const formData = new FormData(this);
                console.log('考勤录入数据:', Object.fromEntries(formData.entries()));
                
                // 可以通过AJAX提交考勤数据
            });
        }
    }
};

/**
 * 项目管理相关功能
 */
const ProjectManager = {
    /**
     * 初始化项目管理页面功能
     */
    init: function() {
        // 绑定项目状态筛选事件
        const statusFilter = document.getElementById('project-status-filter');
        if (statusFilter) {
            statusFilter.addEventListener('change', this.handleStatusFilter);
        }
    },
    
    /**
     * 处理状态筛选
     */
    handleStatusFilter: function() {
        const selectedStatus = this.value;
        console.log(`筛选状态: ${selectedStatus}`);
        
        // 可以通过AJAX加载选定状态的项目
    }
};

/**
 * 页面加载时根据当前页面初始化相应功能
 */
document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;
    
    // 根据当前页面路径初始化相应功能
    if (currentPath.includes('/worker')) {
        WorkerManager.init();
    } else if (currentPath.includes('/attendance')) {
        AttendanceManager.init();
    } else if (currentPath.includes('/project')) {
        ProjectManager.init();
    }
    
    // 初始化工具提示
    initTooltips();
    
    // 添加表单验证样式
    initFormValidation();
});

/**
 * 初始化工具提示
 */
function initTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * 初始化表单验证
 */
function initFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    
    Array.from(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            
            form.classList.add('was-validated');
        }, false);
    });
}

/**
 * 根据当前URL初始化侧边栏菜单激活状态
 */
function initializeActiveMenu() {
    // 获取当前路径
    const currentPath = window.location.pathname;
    
    // 获取所有菜单项
    const menuItems = document.querySelectorAll('.sidebar-menu a');
    
    // 遍历菜单项并设置激活状态
    menuItems.forEach(function(item) {
        // 从href属性中获取链接路径
        const itemPath = item.getAttribute('href');
        
        // 检查当前路径是否包含菜单项路径
        if (currentPath === itemPath || 
            (itemPath !== '/' && currentPath.startsWith(itemPath))) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });
}

/**
 * 初始化提示消息关闭功能
 */
function initializeAlertClosers() {
    // 检查是否加载了Bootstrap
    if (typeof bootstrap !== 'undefined') {
        // 使用Bootstrap的Alert组件
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            new bootstrap.Alert(alert);
        });
    } else {
        // 获取所有关闭按钮
        const closeButtons = document.querySelectorAll('.alert .btn-close');
        
        // 为每个关闭按钮添加点击事件
        closeButtons.forEach(function(button) {
            button.addEventListener('click', function() {
                // 获取父元素（提示消息）并隐藏
                const alert = this.closest('.alert');
                if (alert) {
                    alert.style.display = 'none';
                }
            });
        });
    }
    
    // 设置自动关闭功能（5秒后自动关闭）
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            // 使用Bootstrap的fade效果
            alert.classList.add('fade');
            
            setTimeout(function() {
                // 完全移除元素
                alert.remove();
            }, 500);
        }, 5000);
    });
}

/**
 * 初始化响应式菜单
 */
function initializeResponsiveMenu() {
    // 创建菜单切换按钮（如果不存在）
    if (!document.querySelector('.menu-toggle')) {
        const topBar = document.querySelector('.top-bar');
        if (topBar) {
            const menuToggle = document.createElement('button');
            menuToggle.className = 'menu-toggle';
            menuToggle.innerHTML = '<i class="bi bi-list"></i>';
            topBar.prepend(menuToggle);
        }
    }
    
    // 添加菜单切换事件监听
    const menuToggle = document.querySelector('.menu-toggle');
    if (menuToggle) {
        menuToggle.addEventListener('click', function() {
            const sidebar = document.querySelector('.sidebar');
            if (sidebar) {
                sidebar.classList.toggle('show');
            }
        });
    }
    
    // 点击内容区域时关闭侧边栏（移动设备）
    const mainContent = document.querySelector('.main-content');
    if (mainContent) {
        mainContent.addEventListener('click', function(e) {
            if (window.innerWidth <= 768) {
                const sidebar = document.querySelector('.sidebar');
                if (sidebar && sidebar.classList.contains('show')) {
                    sidebar.classList.remove('show');
                }
            }
        });
    }
}

/**
 * 初始化数据表格交互功能
 */
function initializeDataTables() {
    // 获取所有数据表格
    const tables = document.querySelectorAll('.table');
    
    // 为每个表格添加Bootstrap 5表格样式
    tables.forEach(function(table) {
        if (!table.classList.contains('table-striped')) {
            table.classList.add('table-striped', 'table-hover');
        }
    });
    
    // 获取表格中的操作按钮
    const actionButtons = document.querySelectorAll('.btn-action');
    
    // 为每个操作按钮添加点击事件
    actionButtons.forEach(function(button) {
        button.addEventListener('click', function(event) {
            // 获取按钮上的数据属性
            const action = this.getAttribute('data-action');
            const id = this.getAttribute('data-id');
            
            // 根据不同的操作执行相应的功能
            if (action === 'edit') {
                handleEdit(id);
            } else if (action === 'delete') {
                handleDelete(id, event);
            } else if (action === 'view') {
                handleView(id);
            }
        });
    });
}

/**
 * 处理编辑操作
 * @param {string} id 要编辑的记录ID
 */
function handleEdit(id) {
    // 获取当前页面的基础URL
    const baseUrl = window.location.pathname.replace(/\/list$/, '');
    
    // 跳转到编辑页面
    window.location.href = `${baseUrl}/edit/${id}`;
}

/**
 * 处理删除操作
 * @param {string} id 要删除的记录ID
 * @param {Event} event 点击事件
 */
function handleDelete(id, event) {
    // 阻止默认行为
    event.preventDefault();
    
    // 显示确认对话框
    if (confirm('确定要删除这条记录吗？此操作不可恢复。')) {
        // 获取当前页面的基础URL
        const baseUrl = window.location.pathname.replace(/\/list$/, '');
        
        // 创建表单并提交
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = `${baseUrl}/delete/${id}`;
        
        // 添加CSRF令牌（如果有）
        const csrfToken = document.querySelector('meta[name="_csrf"]');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
        
        if (csrfToken && csrfHeader) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = csrfHeader.content;
            input.value = csrfToken.content;
            form.appendChild(input);
        }
        
        // 将表单添加到文档中并提交
        document.body.appendChild(form);
        form.submit();
    }
}

/**
 * 处理查看操作
 * @param {string} id 要查看的记录ID
 */
function handleView(id) {
    // 获取当前页面的基础URL
    const baseUrl = window.location.pathname.replace(/\/list$/, '');
    
    // 跳转到查看页面
    window.location.href = `${baseUrl}/view/${id}`;
}

/**
 * 切换侧边栏折叠状态
 */
function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');
    
    // 切换侧边栏类名
    sidebar.classList.toggle('collapsed');
    
    // 调整主内容区域的边距
    if (sidebar.classList.contains('collapsed')) {
        mainContent.style.marginLeft = '80px';
    } else {
        mainContent.style.marginLeft = '250px';
    }
}

/**
 * 格式化日期时间
 * @param {string} dateString 日期字符串
 * @param {boolean} showTime 是否显示时间
 * @returns {string} 格式化后的日期时间字符串
 */
function formatDateTime(dateString, showTime = true) {
    if (!dateString) return '';
    
    const date = new Date(dateString);
    
    // 格式化日期部分
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    
    let formatted = `${year}-${month}-${day}`;
    
    // 如果需要显示时间部分
    if (showTime) {
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        formatted += ` ${hours}:${minutes}`;
    }
    
    return formatted;
}

/**
 * 导出表格数据为Excel
 * @param {string} tableId 表格ID
 * @param {string} filename 导出文件名
 */
function exportTableToExcel(tableId, filename = '导出数据') {
    // 获取表格元素
    const table = document.getElementById(tableId);
    if (!table) return;
    
    // 确保SheetJS库已加载
    if (typeof XLSX === 'undefined') {
        console.error('SheetJS库未加载！');
        return;
    }
    
    // 创建工作簿
    const wb = XLSX.utils.table_to_book(table, { sheet: "Sheet1" });
    
    // 导出Excel文件
    XLSX.writeFile(wb, `${filename}.xlsx`);
}

/**
 * 将表单数据转换为JSON对象
 * @param {HTMLFormElement} form 表单元素
 * @returns {Object} JSON对象
 */
function formToJSON(form) {
    const formData = new FormData(form);
    const json = {};
    
    formData.forEach((value, key) => {
        json[key] = value;
    });
    
    return json;
}

/**
 * 发送AJAX请求
 * @param {string} url 请求URL
 * @param {string} method 请求方法
 * @param {Object} data 请求数据
 * @param {Function} successCallback 成功回调函数
 * @param {Function} errorCallback 错误回调函数
 */
function sendAjaxRequest(url, method, data, successCallback, errorCallback) {
    // 创建XMLHttpRequest对象
    const xhr = new XMLHttpRequest();
    
    // 初始化请求
    xhr.open(method, url, true);
    
    // 设置请求头
    xhr.setRequestHeader('Content-Type', 'application/json');
    
    // 获取CSRF令牌（如果有）
    const csrfToken = document.querySelector('meta[name="_csrf"]');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
    
    if (csrfToken && csrfHeader) {
        xhr.setRequestHeader(csrfHeader.content, csrfToken.content);
    }
    
    // 设置响应处理函数
    xhr.onload = function() {
        if (xhr.status >= 200 && xhr.status < 300) {
            // 请求成功
            if (successCallback) {
                successCallback(JSON.parse(xhr.responseText));
            }
        } else {
            // 请求失败
            if (errorCallback) {
                errorCallback(xhr.status, xhr.responseText);
            }
        }
    };
    
    // 设置错误处理函数
    xhr.onerror = function() {
        if (errorCallback) {
            errorCallback(0, '网络错误');
        }
    };
    
    // 发送请求
    xhr.send(JSON.stringify(data));
} 