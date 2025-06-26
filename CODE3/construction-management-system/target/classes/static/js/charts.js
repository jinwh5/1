/**
 * 图表相关功能模块
 * 用于渲染各种统计图表
 * 依赖Chart.js库
 */

// 在文档加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 初始化所有图表
    initializeCharts();
});

/**
 * 初始化所有图表
 */
function initializeCharts() {
    // 检查页面上是否有图表容器
    const chartContainers = document.querySelectorAll('[data-chart]');
    
    // 如果页面上有图表容器，加载Chart.js库
    if (chartContainers.length > 0) {
        // 遍历所有图表容器
        chartContainers.forEach(function(container) {
            // 获取图表类型
            const chartType = container.getAttribute('data-chart');
            
            // 根据图表类型初始化相应的图表
            switch (chartType) {
                case 'worker-stats':
                    initializeWorkerStatsChart(container);
                    break;
                case 'attendance-stats':
                    initializeAttendanceStatsChart(container);
                    break;
                case 'safety-incidents':
                    initializeSafetyIncidentsChart(container);
                    break;
                case 'project-progress':
                    initializeProjectProgressChart(container);
                    break;
                case 'qualification-distribution':
                    initializeQualificationDistributionChart(container);
                    break;
                default:
                    console.warn('未知的图表类型：', chartType);
            }
        });
    }
}

/**
 * 初始化工人统计图表
 * @param {HTMLElement} container 图表容器
 */
function initializeWorkerStatsChart(container) {
    // 获取图表数据
    const chartData = JSON.parse(container.getAttribute('data-chart-data') || '{}');
    
    // 获取canvas元素
    const canvas = container.querySelector('canvas');
    if (!canvas) return;
    
    // 创建图表
    new Chart(canvas.getContext('2d'), {
        type: 'bar',
        data: {
            labels: chartData.labels || ['施工队1', '施工队2', '施工队3', '施工队4', '施工队5'],
            datasets: [{
                label: '工人数量',
                data: chartData.data || [42, 35, 28, 31, 38],
                backgroundColor: 'rgba(54, 162, 235, 0.6)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: '人数'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: '施工队'
                    }
                }
            },
            plugins: {
                title: {
                    display: true,
                    text: '各施工队工人数量统计',
                    font: {
                        size: 16
                    }
                },
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

/**
 * 初始化考勤统计图表
 * @param {HTMLElement} container 图表容器
 */
function initializeAttendanceStatsChart(container) {
    // 获取图表数据
    const chartData = JSON.parse(container.getAttribute('data-chart-data') || '{}');
    
    // 获取canvas元素
    const canvas = container.querySelector('canvas');
    if (!canvas) return;
    
    // 创建图表
    new Chart(canvas.getContext('2d'), {
        type: 'line',
        data: {
            labels: chartData.labels || ['1月', '2月', '3月', '4月', '5月', '6月'],
            datasets: [{
                label: '出勤率',
                data: chartData.data || [92, 89, 93, 87, 90, 94],
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 2,
                tension: 0.2,
                fill: true
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: false,
                    min: 80,
                    max: 100,
                    title: {
                        display: true,
                        text: '出勤率(%)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: '月份'
                    }
                }
            },
            plugins: {
                title: {
                    display: true,
                    text: '月度出勤率统计',
                    font: {
                        size: 16
                    }
                },
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

/**
 * 初始化安全事故统计图表
 * @param {HTMLElement} container 图表容器
 */
function initializeSafetyIncidentsChart(container) {
    // 获取图表数据
    const chartData = JSON.parse(container.getAttribute('data-chart-data') || '{}');
    
    // 获取canvas元素
    const canvas = container.querySelector('canvas');
    if (!canvas) return;
    
    // 创建图表
    new Chart(canvas.getContext('2d'), {
        type: 'pie',
        data: {
            labels: chartData.labels || ['无事故', '轻微事故', '一般事故', '重大事故'],
            datasets: [{
                data: chartData.data || [85, 10, 4, 1],
                backgroundColor: [
                    'rgba(75, 192, 192, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(255, 159, 64, 0.6)',
                    'rgba(255, 99, 132, 0.6)'
                ],
                borderColor: [
                    'rgba(75, 192, 192, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(255, 159, 64, 1)',
                    'rgba(255, 99, 132, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: '安全事故分布统计',
                    font: {
                        size: 16
                    }
                },
                legend: {
                    position: 'bottom'
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const label = context.label || '';
                            const value = context.raw || 0;
                            const total = context.dataset.data.reduce((acc, curr) => acc + curr, 0);
                            const percentage = Math.round((value / total) * 100);
                            return `${label}: ${value} (${percentage}%)`;
                        }
                    }
                }
            }
        }
    });
}

/**
 * 初始化项目进度图表
 * @param {HTMLElement} container 图表容器
 */
function initializeProjectProgressChart(container) {
    // 获取图表数据
    const chartData = JSON.parse(container.getAttribute('data-chart-data') || '{}');
    
    // 获取canvas元素
    const canvas = container.querySelector('canvas');
    if (!canvas) return;
    
    // 创建图表
    new Chart(canvas.getContext('2d'), {
        type: 'bar',
        data: {
            labels: chartData.labels || ['基础工程', '主体结构', '屋面工程', '装饰工程', '设备安装'],
            datasets: [
                {
                    label: '计划进度',
                    data: chartData.planned || [100, 85, 70, 30, 10],
                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                },
                {
                    label: '实际进度',
                    data: chartData.actual || [100, 80, 60, 25, 5],
                    backgroundColor: 'rgba(255, 99, 132, 0.6)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100,
                    title: {
                        display: true,
                        text: '完成百分比(%)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: '工程阶段'
                    }
                }
            },
            plugins: {
                title: {
                    display: true,
                    text: '项目进度对比',
                    font: {
                        size: 16
                    }
                },
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

/**
 * 初始化资质分布图表
 * @param {HTMLElement} container 图表容器
 */
function initializeQualificationDistributionChart(container) {
    // 获取图表数据
    const chartData = JSON.parse(container.getAttribute('data-chart-data') || '{}');
    
    // 获取canvas元素
    const canvas = container.querySelector('canvas');
    if (!canvas) return;
    
    // 创建图表
    new Chart(canvas.getContext('2d'), {
        type: 'doughnut',
        data: {
            labels: chartData.labels || ['特种作业', '高空作业', '电工', '焊工', '普通工人'],
            datasets: [{
                data: chartData.data || [15, 20, 12, 18, 35],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(75, 192, 192, 0.6)',
                    'rgba(153, 102, 255, 0.6)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: '工人资质分布',
                    font: {
                        size: 16
                    }
                },
                legend: {
                    position: 'bottom'
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const label = context.label || '';
                            const value = context.raw || 0;
                            const total = context.dataset.data.reduce((acc, curr) => acc + curr, 0);
                            const percentage = Math.round((value / total) * 100);
                            return `${label}: ${value}人 (${percentage}%)`;
                        }
                    }
                }
            }
        }
    });
}

/**
 * 更新图表数据
 * @param {string} chartId 图表容器ID
 * @param {Object} newData 新的图表数据
 */
function updateChartData(chartId, newData) {
    // 获取图表容器
    const container = document.getElementById(chartId);
    if (!container) return;
    
    // 获取图表类型
    const chartType = container.getAttribute('data-chart');
    
    // 更新容器的数据属性
    container.setAttribute('data-chart-data', JSON.stringify(newData));
    
    // 清除现有图表
    const canvas = container.querySelector('canvas');
    if (!canvas) return;
    
    // 销毁旧的图表实例（如果有）
    const chartInstance = Chart.getChart(canvas);
    if (chartInstance) {
        chartInstance.destroy();
    }
    
    // 根据图表类型重新初始化图表
    switch (chartType) {
        case 'worker-stats':
            initializeWorkerStatsChart(container);
            break;
        case 'attendance-stats':
            initializeAttendanceStatsChart(container);
            break;
        case 'safety-incidents':
            initializeSafetyIncidentsChart(container);
            break;
        case 'project-progress':
            initializeProjectProgressChart(container);
            break;
        case 'qualification-distribution':
            initializeQualificationDistributionChart(container);
            break;
    }
}

/**
 * 根据时间范围获取并更新图表数据
 * @param {string} chartId 图表容器ID
 * @param {string} startDate 开始日期
 * @param {string} endDate 结束日期
 */
function fetchChartDataByDateRange(chartId, startDate, endDate) {
    // 获取图表容器
    const container = document.getElementById(chartId);
    if (!container) return;
    
    // 获取图表类型
    const chartType = container.getAttribute('data-chart');
    
    // 构造API URL
    let apiUrl = '';
    switch (chartType) {
        case 'worker-stats':
            apiUrl = `/api/statistics/workers?startDate=${startDate}&endDate=${endDate}`;
            break;
        case 'attendance-stats':
            apiUrl = `/api/statistics/attendance?startDate=${startDate}&endDate=${endDate}`;
            break;
        case 'safety-incidents':
            apiUrl = `/api/statistics/safety?startDate=${startDate}&endDate=${endDate}`;
            break;
        case 'project-progress':
            apiUrl = `/api/statistics/progress?startDate=${startDate}&endDate=${endDate}`;
            break;
        case 'qualification-distribution':
            apiUrl = `/api/statistics/qualifications?startDate=${startDate}&endDate=${endDate}`;
            break;
        default:
            console.warn('未知的图表类型：', chartType);
            return;
    }
    
    // 发送AJAX请求获取数据
    sendAjaxRequest(
        apiUrl, 
        'GET', 
        null,
        function(response) {
            // 更新图表数据
            updateChartData(chartId, response);
        },
        function(status, error) {
            console.error('获取图表数据失败：', status, error);
        }
    );
}

/**
 * 导出图表为图片
 * @param {string} chartId 图表容器ID
 * @param {string} fileName 文件名（不含扩展名）
 */
function exportChartAsImage(chartId, fileName = '图表') {
    // 获取图表容器
    const container = document.getElementById(chartId);
    if (!container) return;
    
    // 获取canvas元素
    const canvas = container.querySelector('canvas');
    if (!canvas) return;
    
    // 获取图表实例
    const chartInstance = Chart.getChart(canvas);
    if (!chartInstance) return;
    
    // 创建下载链接
    const link = document.createElement('a');
    link.download = `${fileName}.png`;
    link.href = chartInstance.toBase64Image();
    link.click();
} 