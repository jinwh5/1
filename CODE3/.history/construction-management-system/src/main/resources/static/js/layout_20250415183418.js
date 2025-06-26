document.addEventListener('DOMContentLoaded', function() {
    // DOM元素
    const body = document.body;
    const sidebar = document.querySelector('.sidebar');
    const sidebarToggle = document.querySelector('.header-toggler');
    const sidebarClose = document.querySelector('.sidebar-close');
    const overlay = document.querySelector('.overlay');
    const dropdownToggles = document.querySelectorAll('.nav-dropdown-toggle');
    const fullscreenToggle = document.querySelector('.fullscreen-toggle');
    const searchButton = document.querySelector('.btn-search');
    const searchInput = document.querySelector('.search-input');
    const clearSearchButton = document.querySelector('.btn-clear-search');
    
    // 检查是否移动设备
    const isMobile = () => window.innerWidth < 992;
    
    // 侧边栏切换
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            if (isMobile()) {
                // 移动设备：打开/关闭
                body.classList.toggle('sidebar-open');
            } else {
                // 桌面设备：折叠/展开
                body.classList.toggle('sidebar-collapsed');
                
                // 保存状态到本地存储
                localStorage.setItem('sidebar-collapsed', body.classList.contains('sidebar-collapsed'));
            }
        });
    }
    
    // 移动端关闭按钮
    if (sidebarClose) {
        sidebarClose.addEventListener('click', function() {
            body.classList.remove('sidebar-open');
        });
    }
    
    // 点击遮罩层关闭侧边栏
    if (overlay) {
        overlay.addEventListener('click', function() {
            body.classList.remove('sidebar-open');
        });
    }
    
    // 下拉菜单
    if (dropdownToggles) {
        dropdownToggles.forEach(function(toggle) {
            toggle.addEventListener('click', function(e) {
                e.preventDefault();
                
                // 获取当前状态
                const expanded = toggle.getAttribute('aria-expanded') === 'true';
                
                // 如果在桌面折叠模式，不处理点击事件
                if (!isMobile() && body.classList.contains('sidebar-collapsed')) {
                    return;
                }
                
                // 切换状态
                toggle.setAttribute('aria-expanded', !expanded);
                
                // 保存菜单状态到本地存储
                const menuId = toggle.getAttribute('data-target');
                localStorage.setItem(menuId, !expanded);
            });
        });
    }
    
    // 全屏切换
    if (fullscreenToggle) {
        fullscreenToggle.addEventListener('click', function() {
            if (!document.fullscreenElement) {
                if (document.documentElement.requestFullscreen) {
                    document.documentElement.requestFullscreen();
                } else if (document.documentElement.webkitRequestFullscreen) {
                    document.documentElement.webkitRequestFullscreen();
                } else if (document.documentElement.msRequestFullscreen) {
                    document.documentElement.msRequestFullscreen();
                }
                fullscreenToggle.querySelector('i').classList.remove('fa-expand');
                fullscreenToggle.querySelector('i').classList.add('fa-compress');
            } else {
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                } else if (document.webkitExitFullscreen) {
                    document.webkitExitFullscreen();
                } else if (document.msExitFullscreen) {
                    document.msExitFullscreen();
                }
                fullscreenToggle.querySelector('i').classList.remove('fa-compress');
                fullscreenToggle.querySelector('i').classList.add('fa-expand');
            }
        });
        
        // 监听全屏状态变化
        document.addEventListener('fullscreenchange', updateFullscreenButton);
        document.addEventListener('webkitfullscreenchange', updateFullscreenButton);
        document.addEventListener('mozfullscreenchange', updateFullscreenButton);
        document.addEventListener('MSFullscreenChange', updateFullscreenButton);
        
        function updateFullscreenButton() {
            if (document.fullscreenElement) {
                fullscreenToggle.querySelector('i').classList.remove('fa-expand');
                fullscreenToggle.querySelector('i').classList.add('fa-compress');
            } else {
                fullscreenToggle.querySelector('i').classList.remove('fa-compress');
                fullscreenToggle.querySelector('i').classList.add('fa-expand');
            }
        }
    }
    
    // 搜索框功能
    if (searchButton && searchInput) {
        // 移动设备搜索切换
        searchButton.addEventListener('click', function() {
            if (window.innerWidth < 768) {
                body.classList.add('search-active');
                searchInput.focus();
            }
        });
        
        // 清空搜索
        if (clearSearchButton) {
            clearSearchButton.addEventListener('click', function() {
                searchInput.value = '';
                searchInput.focus();
            });
        }
        
        // ESC键关闭搜索
        searchInput.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                body.classList.remove('search-active');
                searchInput.blur();
            }
        });
        
        // 点击外部关闭搜索
        document.addEventListener('click', function(e) {
            if (body.classList.contains('search-active') && 
                !e.target.closest('.search-container') && 
                !e.target.closest('.btn-search')) {
                body.classList.remove('search-active');
            }
        });
    }
    
    // 初始化侧边栏状态
    function initSidebarState() {
        // 读取侧边栏状态
        const sidebarCollapsed = localStorage.getItem('sidebar-collapsed') === 'true';
        if (sidebarCollapsed && !isMobile()) {
            body.classList.add('sidebar-collapsed');
        }
        
        // 读取菜单展开状态
        dropdownToggles.forEach(function(toggle) {
            const menuId = toggle.getAttribute('data-target');
            const expanded = localStorage.getItem(menuId) === 'true';
            
            if (expanded) {
                toggle.setAttribute('aria-expanded', 'true');
            }
        });
    }
    
    // 响应式处理
    function handleResize() {
        if (isMobile()) {
            body.classList.remove('sidebar-collapsed');
            body.classList.remove('sidebar-open');
        }
    }
    
    // 添加活动菜单项高亮
    function setActiveMenuItem() {
        const currentPath = window.location.pathname;
        const navLinks = document.querySelectorAll('.sidebar-nav .nav-link, .sidebar-nav .dropdown-item');
        
        navLinks.forEach(function(link) {
            const href = link.getAttribute('href');
            
            // 检查链接是否匹配当前路径
            if (href === currentPath || 
                (href !== '/' && currentPath.startsWith(href))) {
                
                // 设置链接为活动状态
                link.classList.add('active');
                
                // 如果是子菜单项，展开父菜单
                if (link.classList.contains('dropdown-item')) {
                    const parentItem = link.closest('.nav-item');
                    if (parentItem) {
                        const dropdownToggle = parentItem.querySelector('.nav-dropdown-toggle');
                        if (dropdownToggle) {
                            dropdownToggle.setAttribute('aria-expanded', 'true');
                        }
                    }
                }
            }
        });
    }
    
    // 初始化
    initSidebarState();
    setActiveMenuItem();
    
    // 监听窗口大小变化
    window.addEventListener('resize', handleResize);
    
    // 通知相关功能
    const markReadButtons = document.querySelectorAll('.btn-mark-read');
    if (markReadButtons) {
        markReadButtons.forEach(function(button) {
            button.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                
                // 获取通知项目
                const notificationItem = button.closest('.notification-item');
                if (notificationItem) {
                    // 添加已读样式
                    notificationItem.classList.add('read');
                    
                    // 实际应用中，这里需要发送AJAX请求到服务器
                    // markNotificationAsRead(notificationId);
                    
                    // 更新通知计数
                    updateNotificationCount();
                }
            });
        });
    }
    
    // 全部标记为已读
    const markAllReadButton = document.querySelector('.mark-all-read');
    if (markAllReadButton) {
        markAllReadButton.addEventListener('click', function(e) {
            e.preventDefault();
            
            // 获取所有通知
            const notifications = document.querySelectorAll('.notification-item:not(.read)');
            notifications.forEach(function(notification) {
                notification.classList.add('read');
            });
            
            // 实际应用中，这里需要发送AJAX请求到服务器
            // markAllNotificationsAsRead();
            
            // 更新通知计数
            updateNotificationCount();
        });
    }
    
    // 更新通知计数
    function updateNotificationCount() {
        const badge = document.querySelector('.badge-notification');
        if (badge) {
            const unreadCount = document.querySelectorAll('.notification-item:not(.read)').length;
            
            if (unreadCount > 0) {
                badge.textContent = unreadCount > 99 ? '99+' : unreadCount;
                badge.style.display = 'flex';
            } else {
                badge.style.display = 'none';
            }
        }
    }
}); 