/**
 * 数据可视化模块
 * 用于处理各类图表的渲染和展示
 * 依赖于Chart.js库
 */

/**
 * 初始化所有图表
 */
function initializeCharts() {
    // 检查是否已加载Chart.js库
    if (typeof Chart === 'undefined') {
        console.error('Chart.js库未加载！');
        return;
    }
    
    // 设置全局默认值
    Chart.defaults.font.family = '"思源黑体", "Source Han Sans", "Microsoft YaHei", sans-serif';
    Chart.defaults.color = '#333';
    Chart.defaults.responsive = true;
    Chart.defaults.maintainAspectRatio = false;
    
    // 初始化各个图表
    initializeWorkerDemographics();
    initializeAttendanceChart();
    initializeSafetyIncidentChart();
    initializeProjectProgressChart();
    initializeQualificationDistributionChart();
}

/**
 * 初始化工人人口统计图表
 */
function initializeWorkerDemographics() {
    const workerDemographicsCtx = document.getElementById('workerDemographicsChart');
    if (!workerDemographicsCtx) return;
    
    // 获取工人统计数据
    WorkerService.getWorkerStatistics(function(data) {
        if (!data) return;
        
        // 年龄分布图
        const ageData = {
            labels: ['18-25岁', '26-35岁', '36-45岁', '46-55岁', '56岁以上'],
            datasets: [{
                label: '年龄分布',
                data: data.ageDistribution || [10, 25, 30, 20, 15],
                backgroundColor: [
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(75, 192, 192, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(153, 102, 255, 0.6)'
                ],
                borderColor: [
                    'rgba(54, 162, 235, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(255, 99, 132, 1)',
                    'rgba(153, 102, 255, 1)'
                ],
                borderWidth: 1
            }]
        };
        
        new Chart(workerDemographicsCtx, {
            type: 'pie',
            data: ageData,
            options: {
                plugins: {
                    title: {
                        display: true,
                        text: '工人年龄分布',
                        font: {
                            size: 16,
                            weight: 'bold'
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
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = Math.round((value / total) * 100);
                                return `${label}: ${value}人 (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });
        
        // 性别分布图表可以在这里添加...
    });
}

/**
 * 初始化考勤记录图表
 */
function initializeAttendanceChart() {
    const attendanceChartCtx = document.getElementById('attendanceChart');
    if (!attendanceChartCtx) return;
    
    // 获取过去30天的日期范围
    const endDate = new Date();
    const startDate = new Date();
    startDate.setDate(startDate.getDate() - 30);
    
    // 格式化日期
    const formatDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };
    
    // 获取考勤统计数据
    AttendanceService.getAttendanceStatistics({
        startDate: formatDate(startDate),
        endDate: formatDate(endDate)
    }, function(data) {
        if (!data) return;
        
        const labels = data.dates || [];
        const presentData = data.presentCounts || [];
        const absentData = data.absentCounts || [];
        const lateData = data.lateCounts || [];
        
        new Chart(attendanceChartCtx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: '出勤',
                        data: presentData,
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 2,
                        tension: 0.2,
                        fill: false
                    },
                    {
                        label: '缺勤',
                        data: absentData,
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        borderColor: 'rgba(255, 99, 132, 1)',
                        borderWidth: 2,
                        tension: 0.2,
                        fill: false
                    },
                    {
                        label: '迟到',
                        data: lateData,
                        backgroundColor: 'rgba(255, 206, 86, 0.2)',
                        borderColor: 'rgba(255, 206, 86, 1)',
                        borderWidth: 2,
                        tension: 0.2,
                        fill: false
                    }
                ]
            },
            options: {
                plugins: {
                    title: {
                        display: true,
                        text: '过去30天考勤记录',
                        font: {
                            size: 16,
                            weight: 'bold'
                        }
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '日期'
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: '人数'
                        }
                    }
                }
            }
        });
    });
}

/**
 * 初始化安全事故统计图表
 */
function initializeSafetyIncidentChart() {
    const safetyIncidentChartCtx = document.getElementById('safetyIncidentChart');
    if (!safetyIncidentChartCtx) return;
    
    // 获取过去12个月的日期范围
    const endDate = new Date();
    const startDate = new Date();
    startDate.setMonth(startDate.getMonth() - 11);
    
    // 格式化日期
    const formatDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };
    
    // 获取安全事故统计数据
    SafetyTrainingService.getSafetyIncidentStatistics({
        startDate: formatDate(startDate),
        endDate: formatDate(endDate)
    }, function(data) {
        if (!data) return;
        
        // 获取月份标签
        const months = [];
        const currentDate = new Date(startDate);
        while (currentDate <= endDate) {
            months.push(`${currentDate.getFullYear()}年${currentDate.getMonth() + 1}月`);
            currentDate.setMonth(currentDate.getMonth() + 1);
        }
        
        // 事故类型和对应的颜色
        const incidentTypes = data.incidentTypes || ['轻微事故', '一般事故', '重大事故', '特大事故'];
        const colors = [
            'rgba(75, 192, 192, 1)',  // 轻微事故 - 青色
            'rgba(255, 206, 86, 1)',  // 一般事故 - 黄色
            'rgba(255, 159, 64, 1)',  // 重大事故 - 橙色
            'rgba(255, 99, 132, 1)'   // 特大事故 - 红色
        ];
        
        // 准备数据集
        const datasets = [];
        incidentTypes.forEach((type, index) => {
            datasets.push({
                label: type,
                data: data.countsPerType && data.countsPerType[type] || new Array(months.length).fill(0),
                backgroundColor: colors[index],
                borderColor: colors[index],
                borderWidth: 1
            });
        });
        
        new Chart(safetyIncidentChartCtx, {
            type: 'bar',
            data: {
                labels: months,
                datasets: datasets
            },
            options: {
                plugins: {
                    title: {
                        display: true,
                        text: '过去12个月安全事故统计',
                        font: {
                            size: 16,
                            weight: 'bold'
                        }
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '月份'
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: '事故数量'
                        },
                        stacked: true
                    }
                }
            }
        });
    });
}

/**
 * 初始化项目进度图表
 */
function initializeProjectProgressChart() {
    const progressChartCtx = document.getElementById('projectProgressChart');
    if (!progressChartCtx) return;
    
    // 获取项目进度数据
    ProjectProgressService.getProjectProgress(function(data) {
        if (!data || !Array.isArray(data)) return;
        
        // 处理项目进度数据
        const projects = data.map(item => item.projectName || '未命名项目');
        const plannedProgress = data.map(item => item.plannedProgress || 0);
        const actualProgress = data.map(item => item.actualProgress || 0);
        
        new Chart(progressChartCtx, {
            type: 'bar',
            data: {
                labels: projects,
                datasets: [
                    {
                        label: '计划进度',
                        data: plannedProgress,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    },
                    {
                        label: '实际进度',
                        data: actualProgress,
                        backgroundColor: 'rgba(75, 192, 192, 0.6)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }
                ]
            },
            options: {
                indexAxis: 'y',
                plugins: {
                    title: {
                        display: true,
                        text: '项目进度对比',
                        font: {
                            size: 16,
                            weight: 'bold'
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const label = context.dataset.label || '';
                                const value = context.raw || 0;
                                return `${label}: ${value}%`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        beginAtZero: true,
                        max: 100,
                        title: {
                            display: true,
                            text: '完成百分比 (%)'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: '项目名称'
                        }
                    }
                }
            }
        });
    });
}

/**
 * 初始化资质证书分布图表
 */
function initializeQualificationDistributionChart() {
    const qualificationChartCtx = document.getElementById('qualificationDistributionChart');
    if (!qualificationChartCtx) return;
    
    // 获取资质证书分布数据
    QualificationService.getQualificationDistribution(function(data) {
        if (!data) return;
        
        // 提取数据
        const labels = data.qualificationTypes || [];
        const counts = data.counts || [];
        
        // 生成渐变色
        const colorPalette = [
            'rgba(255, 99, 132, 0.8)',
            'rgba(54, 162, 235, 0.8)',
            'rgba(255, 206, 86, 0.8)',
            'rgba(75, 192, 192, 0.8)',
            'rgba(153, 102, 255, 0.8)',
            'rgba(255, 159, 64, 0.8)',
            'rgba(199, 199, 199, 0.8)'
        ];
        
        // 如果类型数量超出了颜色数量，循环使用颜色
        const backgroundColors = labels.map((_, i) => colorPalette[i % colorPalette.length]);
        
        new Chart(qualificationChartCtx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    label: '证书数量',
                    data: counts,
                    backgroundColor: backgroundColors,
                    borderColor: backgroundColors.map(color => color.replace('0.8', '1')),
                    borderWidth: 1
                }]
            },
            options: {
                plugins: {
                    title: {
                        display: true,
                        text: '资质证书类型分布',
                        font: {
                            size: 16,
                            weight: 'bold'
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
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = Math.round((value / total) * 100);
                                return `${label}: ${value}张 (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });
    });
}

/**
 * 更新日期范围内的图表数据
 * @param {string} chartId 图表元素ID
 * @param {Date} startDate 开始日期
 * @param {Date} endDate 结束日期
 */
function updateChartDateRange(chartId, startDate, endDate) {
    // 格式化日期
    const formatDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };
    
    // 根据图表类型更新数据
    switch(chartId) {
        case 'attendanceChart':
            AttendanceService.getAttendanceStatistics({
                startDate: formatDate(startDate),
                endDate: formatDate(endDate)
            }, function(data) {
                if (!data) return;
                
                // 获取图表实例并更新数据
                const chart = Chart.getChart('attendanceChart');
                if (chart) {
                    chart.data.labels = data.dates || [];
                    chart.data.datasets[0].data = data.presentCounts || [];
                    chart.data.datasets[1].data = data.absentCounts || [];
                    chart.data.datasets[2].data = data.lateCounts || [];
                    chart.update();
                }
            });
            break;
            
        case 'safetyIncidentChart':
            SafetyTrainingService.getSafetyIncidentStatistics({
                startDate: formatDate(startDate),
                endDate: formatDate(endDate)
            }, function(data) {
                if (!data) return;
                
                // 更新图表数据
                // 实现类似于初始化图表时的逻辑
                const chart = Chart.getChart('safetyIncidentChart');
                if (chart && data.incidentTypes) {
                    // 更新数据...
                    chart.update();
                }
            });
            break;
            
        // 可以添加其他图表的更新逻辑
    }
}

/**
 * 导出图表为图片
 * @param {string} chartId 图表元素ID
 * @param {string} fileName 文件名（不包含扩展名）
 */
function exportChartAsImage(chartId, fileName) {
    const chart = Chart.getChart(chartId);
    if (!chart) {
        console.error(`图表 ${chartId} 未找到`);
        return;
    }
    
    // 创建一个链接并触发下载
    const link = document.createElement('a');
    link.download = `${fileName || chartId}.png`;
    link.href = chart.toBase64Image();
    link.click();
}

/**
 * 初始化日期范围选择器
 * @param {string} chartId 关联的图表ID
 */
function initializeDateRangePicker(chartId) {
    const dateRangePicker = document.getElementById(`${chartId}DateRange`);
    if (!dateRangePicker) return;
    
    // 假设使用了datepicker库
    $(dateRangePicker).daterangepicker({
        startDate: moment().subtract(30, 'days'),
        endDate: moment(),
        ranges: {
            '过去7天': [moment().subtract(6, 'days'), moment()],
            '过去30天': [moment().subtract(29, 'days'), moment()],
            '本月': [moment().startOf('month'), moment().endOf('month')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        locale: {
            format: 'YYYY-MM-DD',
            applyLabel: '确认',
            cancelLabel: '取消',
            customRangeLabel: '自定义范围'
        }
    }, function(start, end) {
        // 更新图表数据
        updateChartDateRange(chartId, start.toDate(), end.toDate());
    });
}

// 文档加载完成后初始化所有图表
document.addEventListener('DOMContentLoaded', function() {
    // 初始化图表
    initializeCharts();
    
    // 初始化日期范围选择器
    initializeDateRangePicker('attendanceChart');
    initializeDateRangePicker('safetyIncidentChart');
    
    // 设置导出按钮点击事件
    const exportButtons = document.querySelectorAll('.export-chart-btn');
    exportButtons.forEach(button => {
        button.addEventListener('click', function() {
            const chartId = this.dataset.chartId;
            const chartName = this.dataset.chartName || chartId;
            exportChartAsImage(chartId, chartName);
        });
    });
}); 