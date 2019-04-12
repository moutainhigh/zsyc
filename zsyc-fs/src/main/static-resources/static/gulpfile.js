'use strict';

var fs = require('fs');
var gulp = require('gulp');
var maps = require('gulp-sourcemaps');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var rename = require('gulp-rename');
var del = require('del');
var argv = require('yargs').argv;

gulp.task('clean', function (cb) {
  return del([
    'dist/**/*',
    'js/dist/*',
    'css/*'
  ],cb);
});

gulp.task('build', ['clean'], function(cb){
	return gulp.watch(['framework' , 'js' , 'sass'],cb);
	//return runSequence(['framework' , 'js' , 'sass' , 'less'],cb);
});

gulp.task('sass', function() {
	return gulp.src('./sass/**/*.scss')
		.pipe(maps.init())
		.pipe(sass().on('error', sass.logError))
		.pipe(pref('last 2 versions'))
		//.pipe(rename({suffix: '.min'}))
        .pipe(cleanCSS())
		.pipe(maps.write('.'))
		.pipe(gulp.dest('./css'));
});



gulp.task('js',function(){
	return gulp.src([ 'bower_components/js-sha1/src/sha1.js', 'js/file.js'])
		.pipe(maps.init())
		.pipe(concat('file.js'))
		.pipe(gulp.dest('assets/js'))
        .pipe(rename({suffix: '.min'}))
        .pipe(uglify())
        .pipe(maps.write('.'))
        .pipe(gulp.dest('assets/js'));
});

gulp.task('watch',[ 'clean' , 'sass' ] ,function() {
	gulp.watch('./sass/**/*.scss', ['sass']);
	//gulp.watch('./js/**/*.js', ['js']);
});


gulp.task('default', ['watch']);
