// 等待DOM加载完成
document.addEventListener('DOMContentLoaded', function() {
    // 更新当前日期和时间
    updateCurrentDateTime();
    setInterval(updateCurrentDateTime, 60000); // 每分钟更新一次
    
    // 初始化侧边栏菜单激活状态
    initializeActiveMenu();
    
    // 初始化响应式菜单
    initializeResponsiveMenu();
    
    // 初始化工具提示
    initializeTooltips();
    
    // 初始化弹出提示
    initializePopovers();
    
    // 初始化表单验证
    initializeFormValidation();
    
    // 初始化数据表格
    initializeDataTables();
    
    // 设置页面所有按钮的波纹效果
    initializeRippleEffect();
});

/**
 * 更新当前日期和时间显示
 */
function updateCurrentDateTime() {
    const dateElement = document.getElementById('current-date');
    if (!dateElement) return;
    
    const now = new Date();
    const options = { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric', 
        hour: '2-digit', 
        minute: '2-digit',
        hour12: false
    };
    dateElement.textContent = now.toLocaleDateString('zh-CN', options);
}

/**
 * 初始化响应式菜单
 */
function initializeResponsiveMenu() {
    // 添加侧边栏切换按钮事件
    const sidebarToggler = document.querySelector('.sidebar-toggler');
    if (sidebarToggler) {
        sidebarToggler.addEventListener('click', function() {
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
            if (window.innerWidth <= 991.98) {
                const sidebar = document.querySelector('.sidebar');
                if (sidebar && sidebar.classList.contains('show') && 
                    !e.target.closest('.sidebar-toggler')) {
                    sidebar.classList.remove('show');
                }
            }
        });
    }
    
    // 窗口大小变化时处理
    window.addEventListener('resize', function() {
        if (window.innerWidth > 991.98) {
            const sidebar = document.querySelector('.sidebar');
            if (sidebar) {
                sidebar.classList.remove('show');
            }
        }
    });
}

/**
 * 初始化Bootstrap 5工具提示
 */
function initializeTooltips() {
    // 初始化所有工具提示
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * 初始化Bootstrap 5弹出提示
 */
function initializePopovers() {
    // 初始化所有弹出提示
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function(popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

/**
 * 根据当前URL初始化侧边栏菜单激活状态
 */
function initializeActiveMenu() {
    // 获取当前路径
    const currentPath = window.location.pathname;
    
    // 获取所有菜单项
    const menuItems = document.querySelectorAll('.sidebar .nav-link');
    
    // 遍历菜单项并设置激活状态
    menuItems.forEach(function(item) {
        // 从href属性中获取链接路径
        const itemPath = item.getAttribute('href');
        
        // 检查当前路径是否匹配菜单项路径
        if (currentPath === itemPath || 
            (itemPath !== '/' && currentPath.startsWith(itemPath))) {
            // 添加激活类
            item.classList.add('active');
            item.classList.remove('text-white');
        } else {
            // 移除激活类
            item.classList.remove('active');
            item.classList.add('text-white');
        }
    });
}

/**
 * 初始化表单验证
 */
function initializeFormValidation() {
    // 获取所有需要验证的表单
    const forms = document.querySelectorAll('.needs-validation');
    
    // 为每个表单添加提交事件监听
    Array.from(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            // 如果表单未通过HTML5验证，阻止提交
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                
                // 显示第一个错误输入的提示
                const invalidInputs = form.querySelectorAll(':invalid');
                if (invalidInputs.length > 0) {
                    invalidInputs[0].focus();
                }
            }
            
            // 添加验证样式类
            form.classList.add('was-validated');
        }, false);
    });
    
    // 实时验证
    const inputs = document.querySelectorAll('.form-control, .form-select, .form-check-input');
    inputs.forEach(function(input) {
        input.addEventListener('input', function() {
            // 验证单个输入
            if (this.checkValidity()) {
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');
            } else {
                this.classList.remove('is-valid');
                this.classList.add('is-invalid');
            }
        });
    });
}

/**
 * 初始化数据表格交互功能
 */
function initializeDataTables() {
    // 初始化所有表格的基本功能（如果已经加载了DataTables库）
    if (typeof $.fn.DataTable !== 'undefined') {
        $('.datatable').DataTable({
            language: {
                url: '/js/plugins/dataTables.chinese.json'
            },
            responsive: true,
            pageLength: 10,
            dom: '<"row mb-3"<"col-md-6"B><"col-md-6"f>>' +
                 '<"row"<"col-md-12"tr>>' +
                 '<"row"<"col-md-5"i><"col-md-7"p>>',
            buttons: [
                {
                    extend: 'excel',
                    text: '<i class="bi bi-file-excel me-1"></i>导出Excel',
                    className: 'btn btn-sm btn-success'
                },
                {
                    extend: 'pdf',
                    text: '<i class="bi bi-file-pdf me-1"></i>导出PDF',
                    className: 'btn btn-sm btn-danger'
                },
                {
                    extend: 'print',
                    text: '<i class="bi bi-printer me-1"></i>打印',
                    className: 'btn btn-sm btn-primary'
                }
            ]
        });
    } else {
        // 添加基本的表格样式
        const tables = document.querySelectorAll('.table');
        tables.forEach(function(table) {
            if (!table.classList.contains('table-striped')) {
                table.classList.add('table-striped', 'table-hover');
            }
        });
    }
}

/**
 * 初始化按钮波纹效果
 */
function initializeRippleEffect() {
    // 获取所有按钮
    const buttons = document.querySelectorAll('.btn:not(.no-ripple)');
    
    // 为每个按钮添加波纹效果
    buttons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            // 创建波纹元素
            const ripple = document.createElement('span');
            ripple.classList.add('ripple');
            this.appendChild(ripple);
            
            // 设置波纹大小和位置
            const diameter = Math.max(this.clientWidth, this.clientHeight);
            ripple.style.width = ripple.style.height = diameter + 'px';
            
            const rect = this.getBoundingClientRect();
            ripple.style.left = (e.clientX - rect.left - diameter / 2) + 'px';
            ripple.style.top = (e.clientY - rect.top - diameter / 2) + 'px';
            
            // 移除波纹元素
            setTimeout(function() {
                ripple.remove();
            }, 600);
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
    event.preventDefault();
    
    // 使用Bootstrap模态框确认
    const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    const confirmButton = document.getElementById('confirmDelete');
    
    // 设置确认按钮的事件
    confirmButton.onclick = function() {
        // 隐藏模态框
        modal.hide();
        
        // 显示加载状态
        const loadingOverlay = document.createElement('div');
        loadingOverlay.className = 'loading-overlay';
        loadingOverlay.innerHTML = `
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">正在处理...</span>
            </div>
        `;
        document.body.appendChild(loadingOverlay);
        
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
    };
    
    // 显示模态框
    modal.show();
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
 * 格式化货币
 * @param {number} amount 金额
 * @param {string} currency 货币类型，默认为人民币
 * @returns {string} 格式化后的货币字符串
 */
function formatCurrency(amount, currency = 'CNY') {
    // 根据货币类型设置格式选项
    const options = {
        style: 'currency',
        currency: currency,
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    };
    
    // 使用Intl.NumberFormat进行格式化
    return new Intl.NumberFormat('zh-CN', options).format(amount);
}

/**
 * 显示通知消息
 * @param {string} message 消息内容
 * @param {string} type 消息类型：success, info, warning, danger
 * @param {number} duration 显示时长（毫秒）
 */
function showNotification(message, type = 'info', duration = 5000) {
    // 创建通知元素
    const notification = document.createElement('div');
    notification.className = `toast align-items-center text-white bg-${type} border-0`;
    notification.setAttribute('role', 'alert');
    notification.setAttribute('aria-live', 'assertive');
    notification.setAttribute('aria-atomic', 'true');
    
    // 设置通知内容
    notification.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                <i class="bi bi-${getIconForType(type)} me-2"></i>
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="关闭"></button>
        </div>
    `;
    
    // 创建通知容器（如果不存在）
    let toastContainer = document.querySelector('.toast-container');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    }
    
    // 将通知添加到容器
    toastContainer.appendChild(notification);
    
    // 创建Bootstrap Toast实例并显示
    const toast = new bootstrap.Toast(notification, {
        autohide: true,
        delay: duration
    });
    toast.show();
    
    // 通知关闭后移除元素
    notification.addEventListener('hidden.bs.toast', function() {
        notification.remove();
    });
    
    // 根据消息类型获取图标
    function getIconForType(type) {
        switch (type) {
            case 'success': return 'check-circle-fill';
            case 'danger': return 'exclamation-triangle-fill';
            case 'warning': return 'exclamation-circle-fill';
            case 'info':
            default: return 'info-circle-fill';
        }
    }
}

/**
 * 显示加载提示
 * @param {boolean} show 是否显示
 * @param {string} message 加载提示消息
 */
function showLoading(show = true, message = '加载中...') {
    // 获取或创建加载提示元素
    let loadingOverlay = document.querySelector('.loading-overlay');
    
    if (show) {
        // 如果不存在，创建加载提示元素
        if (!loadingOverlay) {
            loadingOverlay = document.createElement('div');
            loadingOverlay.className = 'loading-overlay';
            loadingOverlay.innerHTML = `
                <div class="spinner-border text-primary" role="status"></div>
                <p class="mt-2">${message}</p>
            `;
            document.body.appendChild(loadingOverlay);
        }
    } else {
        // 如果存在，移除加载提示元素
        if (loadingOverlay) {
            loadingOverlay.remove();
        }
    }
} 