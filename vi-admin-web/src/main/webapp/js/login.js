var bus = new Vue()

function emitError(message) {
    bus.$emit('error', message)
}

function onError(fn) {
    bus.$on('error', fn)
}

function emitInfo(message) {
    bus.$emit('info', message)
}

function onInfo(fn) {
    bus.$on('info', fn)
}

function emitLogin() {
    bus.$emit('login')
}

function onLogin(fn) {
    bus.$on('login', fn)
}

function handleError(errorMessage) {
    emitError(errorMessage)
}

var Alert = {
    template: '#alert',
    data: function () {
        return {
            classObject: {
                'is-danger': false,
                'is-info': false
            },
            message: '',
            visible: false
        }
    },
    mounted: function () {
        onError(function (errorMessage) {
            this.classObject['is-danger'] = true;
            this.message = errorMessage
            this.visible = true
            setTimeout(this.reset.bind(this), 2000)
        }.bind(this))

        onInfo(function (message) {
            this.classObject['is-info'] = true;
            this.message = message
            this.visible = true
            setTimeout(this.reset.bind(this), 2000)
        }.bind(this))
    },
    methods: {
        reset: function () {
            this.visible = false
            this.classObject = {}
        }
    }
}

//局部组件<x-header></x-header>配置选项
var Header = {
    template: '#header',
    data: function () {
        return {
            currentUser: JSON.parse(sessionStorage.getItem('currentUser'))
            //currentUser: JSON.parse(Cookies.get('currentUser'))
        }
    },
    mounted: function () {
        onLogin(function () {
            this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
            //this.currentUser = JSON.parse(Cookies.get('currentUser'));
        }.bind(this))
    },
    methods: {
        logout: function () {
            this.currentUser = null
            sessionStorage.removeItem('token')
            sessionStorage.removeItem('currentUser')

            //Cookies.remove('token')
            //Cookies.remove('currentUser')
            this.$router.push('/')
        }
    }
}

var PostList = {
    template: '#posts',
    data: function () {
        return {
            posts: []
        }
    },
    filters: {
        longToDate: function (long) {
            var date = new Date(long)
            return date.toLocaleString()
        }
    },
    methods: {
        showDetail: {}
    }
}

// 1. 定义（路由）组件。
var LoginForm = {
    template: '#login-form',
    data: function () {
        return {
            username: '',
            password: ''
        }
    },
    methods: {
        handleSubmit: function (e) {
            e.preventDefault()
            if (this.username === '') {
                handleError('用户名不能为空')
                return false
            }
            if (this.password === '') {
                handleError('密码不能为空')
                return false
            }
            axios.post('/vi/api/authentication', {
                name: this.username,
                password: this.password
            }).then(function (res) {
                if (res.data.error) {
                    handleError(res.data.error)
                } else {
                    Cookies.set('username', this.username,{ expires: 15});
                    Cookies.set('password', this.password,{ expires: 15});
                    emitLogin()
                    this.$router.push('/')
                }
            }.bind(this))
        }
    }
}

var SignupForm = {
    template: '#signup-form',
    data: function () {
        return {
            name: '',
            password: '',
            passwordAgain: ''
        }
    },
    methods: {
        handleSubmit: function (e) {
            e.preventDefault()
            if (this.name === '') {
                handleError('用户名不能为空')
                return false
            }
            if (this.password === '') {
                handleError('密码不能为空')
                return false
            }
            if (this.password !== this.passwordAgain) {
                handleError('两次输入的密码不一致')
                return false
            }
            axios.post('/vi/api/user', {
                name: this.name,
                password: this.password
            }).then(function (res) {
                if (res.data.error) {
                    handleError(res.data.error)
                } else {
                    emitInfo('注册成功，请登录')
                    this.$router.push('/login')
                }
            }.bind(this))
        }
    }
}

// 2. 定义路由，每个路由应该映射一个组件
var routes = [
    {path: '/', component: PostList},
    {path: '/login', component: LoginForm},
    {path: '/signup', component: SignupForm},
]

// 3. 创建 router 实例，然后传 `routes` 配置
var router = new VueRouter({
    routes: routes
    //routes // （缩写）相当于 routes: routes
})

// 4. 创建和挂载根实例。
//Vue 实例还提供了一些有用的实例属性与方法。它们都有前缀 $，以便与用户定义的属性区分开来
//Vue 指令是带有 v- 前缀的特殊属性，参数在指令后以冒号指明，修饰符是以半角句号 . 指明的特殊后缀
new Vue({ //实例化 Vue 构造器
    router: router, //通过 router 配置参数注入路由，从而让整个应用都有路由功能
    components: { //注册局部组件，将只在父模板可用
        'x-header': Header, //x-header为组件名、Header为配置选项，注册后，可以使用<x-header></x-header>来调用组件
        'x-alert': Alert,
    }
}).$mount('#app') //挂载根实例
