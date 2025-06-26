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