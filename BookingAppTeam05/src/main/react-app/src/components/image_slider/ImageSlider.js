// source code from https://github.com/briancodex/react-image-slider-carousel

import React, { useState } from 'react';
import { FaArrowAltCircleRight, FaArrowAltCircleLeft } from 'react-icons/fa';
import styles from './image_slider.module.css';


const ImageSlider = ({ slides }) => {
  const [current, setCurrent] = useState(0);
  const length = slides.length;

  const nextSlide = () => {
    setCurrent(current === length - 1 ? 0 : current + 1);
  };

  const prevSlide = () => {
    setCurrent(current === 0 ? length - 1 : current - 1);
  };

  if (!Array.isArray(slides) || slides.length <= 0) {
    return null;
  }

  return (
    <section className='slider'>
      <FaArrowAltCircleLeft className={styles.leftArrow} onClick={prevSlide} />
      <FaArrowAltCircleRight className={styles.rightArrow} onClick={nextSlide} />
      {slides.map((slide, index) => {
        return (
          <div
            className={index === current ? styles.slideActive : styles.slide}
            key={index}
          >
            {index === current && (
              <img src={slide.image} alt='travel image' className={styles.image} />
            )}
          </div>
        );
      })}
    </section>
  );
};

export default ImageSlider;